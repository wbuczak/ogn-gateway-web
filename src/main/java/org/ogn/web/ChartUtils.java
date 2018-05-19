package org.ogn.web;

import java.awt.Color;
import java.awt.GradientPaint;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.ogn.gateway.plugin.stats.dao.StatsRecordField;

public class ChartUtils {

	public static void drawChart(HttpServletResponse response, JFreeChart chart, int width, int height) {
		try {
			ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, width, height);
			response.flushBuffer();
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	static class LastValueXYItemLabelGenerator extends StandardXYItemLabelGenerator {
		private static final long serialVersionUID = 1L;

		@Override
		public String generateLabel(XYDataset dataset, int series, int item) {
			if (item == dataset.getItemCount(0) - 1)
				return "" + dataset.getY(0, item).intValue();
			else
				return "";
		}
	}

	public static JFreeChart createTimeSeriesChart(XYDataset dataset, String chartTitle, String yLabel) {
		final JFreeChart chart =
				ChartFactory.createTimeSeriesChart(chartTitle, "date", yLabel, dataset, true, false, false);

		final XYPlot plot = chart.getXYPlot();

		plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		final DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-YY"));

		plot.getRenderer().setBaseItemLabelGenerator(new LastValueXYItemLabelGenerator());
		plot.getRenderer().setBaseItemLabelsVisible(true);

		return chart;
	}

	public static XYDataset createXYDataSet(List<Map<String, Object>> dailyStats, final String timeSeriesName,
			final String field) {
		final TimeSeries s1 = new TimeSeries(timeSeriesName);

		for (final Map<String, Object> r : dailyStats) {
			final Instant timestamp = Instant.ofEpochMilli((long) r.get(StatsRecordField.DATE.getValue()));
			final LocalDateTime datetime = LocalDateTime.ofInstant(timestamp, ZoneOffset.UTC);

			s1.add(new Day(datetime.getDayOfMonth(), datetime.getMonthValue(), datetime.getYear()), (int) r.get(field));
		}

		final TimeSeries mav = MovingAverage.createMovingAverage(s1, "moving avg(30)", 30, 3);

		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(s1);
		dataset.addSeries(mav);
		return dataset;
	}

	public static JFreeChart createBarChart(CategoryDataset categorydataset, String chartTitle, String[] axeLabels) {
		final JFreeChart jfreechart = ChartFactory.createBarChart(chartTitle, axeLabels[0], axeLabels[1],
				categorydataset, PlotOrientation.HORIZONTAL, false, true, false);
		jfreechart.setBackgroundPaint(Color.white);
		final CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();

		categoryplot.setBackgroundPaint(Color.lightGray);
		categoryplot.setDomainGridlinePaint(Color.white);
		categoryplot.setDomainGridlinesVisible(true);
		categoryplot.setRangeGridlinePaint(Color.white);
		categoryplot.getDomainAxis().setMaximumCategoryLabelWidthRatio(0.4F);
		final NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		final BarRenderer barrenderer = (BarRenderer) categoryplot.getRenderer();
		barrenderer.setDrawBarOutline(false);
		final GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
		barrenderer.setSeriesPaint(0, gradientpaint);

		return jfreechart;
	}

	public static CategoryDataset createCategoryDataset(List<Map<String, Object>> topList, ChartType type) {
		final DefaultCategoryDataset dset = new DefaultCategoryDataset();

		switch (type) {

			case TOP_RECEIVERS_BY_RANGE:

				for (final Map<String, Object> r : topList) {
					dset.addValue((float) r.get(StatsRecordField.RANGE.getValue()), "S1",
							(String) r.get(StatsRecordField.RECEIVER_NAME.getValue()));
				}

				break;

			case TOP_RECEIVERS_BY_NUMBER_OF_RECEPTIONS:

				for (final Map<String, Object> r : topList) {
					dset.addValue((int) r.get(StatsRecordField.COUNT.getValue()), "S1",
							(String) r.get(StatsRecordField.RECEIVER_NAME.getValue()));
				}

				break;

			case TOP_RECEIVERS_BY_MAX_RECEPTION_ALT:

				for (final Map<String, Object> r : topList) {
					dset.addValue((float) r.get(StatsRecordField.ALT.getValue()), "S1",
							(String) r.get(StatsRecordField.RECEIVER_NAME.getValue()));
				}

				break;

		}// switch

		return dset;
	}
}
