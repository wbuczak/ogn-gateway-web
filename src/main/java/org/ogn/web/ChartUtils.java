package org.ogn.web;

import java.awt.Color;
import java.awt.GradientPaint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
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

public class ChartUtils {

	public static void drawChart(HttpServletResponse response, JFreeChart chart, int width, int height) {
		try {
			ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, width, height);
			response.flushBuffer();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static JFreeChart createTimeSeriesChart(XYDataset dataset, String title) {
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "date", "active receivers", dataset, true,
				false, false);

		XYPlot plot = chart.getXYPlot();
		plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-YY"));

		return chart;
	}

	public static XYDataset createXYDataSet(List<Map<String, Object>> activeReceivers) {

		TimeSeries s1 = new TimeSeries("OGN Active Receivers");

		Calendar cal = Calendar.getInstance();

		for (Map<String, Object> r : activeReceivers) {
			cal.setTimeInMillis((long) r.get("date"));
			s1.add(new Day(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)),
					(int) r.get("count"));
		}

		final TimeSeries mav = MovingAverage.createMovingAverage(s1, "average", 30, 3);

		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(s1);
		dataset.addSeries(mav);
		return dataset;
	}

	public static JFreeChart createBarChart(CategoryDataset categorydataset, String chartTitle, String[] axeLabels) {
		JFreeChart jfreechart = ChartFactory.createBarChart(chartTitle, axeLabels[0], axeLabels[1], categorydataset,
				PlotOrientation.HORIZONTAL, false, true, false);
		jfreechart.setBackgroundPaint(Color.white);
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		categoryplot.setBackgroundPaint(Color.lightGray);
		categoryplot.setDomainGridlinePaint(Color.white);
		categoryplot.setDomainGridlinesVisible(true);
		categoryplot.setRangeGridlinePaint(Color.white);
		categoryplot.getDomainAxis().setMaximumCategoryLabelWidthRatio(0.4F);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		BarRenderer barrenderer = (BarRenderer) categoryplot.getRenderer();
		barrenderer.setDrawBarOutline(false);
		GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, Color.blue, 0.0F, 0.0F, new Color(0, 0, 64));
		barrenderer.setSeriesPaint(0, gradientpaint);

		return jfreechart;
	}

	public static CategoryDataset createCategoryDataset(List<Map<String, Object>> topList, ChartType type) {
		DefaultCategoryDataset dset = new DefaultCategoryDataset();

		switch (type) {

		case TOP_RECEIVERS_BY_RANGE:

			for (Map<String, Object> r : topList) {
				dset.addValue((float) r.get("range"), "S1", (String) r.get("receiver_name"));
			}

			break;

		case TOP_RECEIVERS_BY_NUMBER_OF_RECEPTIONS:

			for (Map<String, Object> r : topList) {
				dset.addValue((int) r.get("count"), "S1", (String) r.get("receiver_name"));
			}

			break;

		case TOP_RECEIVERS_BY_MAX_RECEPTION_ALT:

			for (Map<String, Object> r : topList) {
				dset.addValue((int) r.get("max_alt"), "S1", (String) r.get("receiver_name"));
			}

			break;

		}// switch

		return dset;
	}
}
