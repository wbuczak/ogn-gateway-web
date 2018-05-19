package org.ogn.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.ogn.gateway.plugin.stats.TimeDateUtils;
import org.ogn.gateway.plugin.stats.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/charts")
public class ChartController {

	private StatsService service;

	@Autowired
	public void setReceiversService(StatsService service) {
		this.service = service;
	}

	@RequestMapping(value = "/online-rec", method = RequestMethod.GET)
	public void drawActiveReceiversChart(HttpServletResponse response) {
		drawActiveReceiversChart(response, 30);
	}

	@RequestMapping(value = "/online-rec/{days}", method = RequestMethod.GET)
	public void drawActiveReceiversChart(HttpServletResponse response, @PathVariable("days") int days) {
		response.setContentType("image/png");

		final List<Map<String, Object>> dailyStats = service.getDailyStatsForDays(days);

		final XYDataset dataset = ChartUtils.createXYDataSet(dailyStats, "OGN online receivers", "online_receivers");

		final JFreeChart chart = ChartUtils.createTimeSeriesChart(dataset, "OGN online receivers", "online receivers");

		ChartUtils.drawChart(response, chart, 760, 420);
	}

	@RequestMapping(value = "/dist-aircraft/{days}", method = RequestMethod.GET)
	public void drawOnlineReceiversChart(HttpServletResponse response, @PathVariable("days") int days) {
		response.setContentType("image/png");

		final List<Map<String, Object>> dailyStats = service.getDailyStatsForDays(days);

		final XYDataset dataset =
				ChartUtils.createXYDataSet(dailyStats, "no. of distinct aircraft", "unique_aircraft_ids");
		final JFreeChart chart =
				ChartUtils.createTimeSeriesChart(dataset, "Distinct aircraft received", "distinct aircraft ids");

		ChartUtils.drawChart(response, chart, 760, 420);
	}

	@RequestMapping(value = "/toprec-range/{limit}", method = RequestMethod.GET)
	public void drawTopRangeReceiversChart(HttpServletResponse response, @PathVariable("limit") int limit) {
		drawTopRangeReceiversChart(response, null, limit);
	}

	@RequestMapping(value = "/toprec-range/{date}/{limit}", method = RequestMethod.GET)
	public void drawTopRangeReceiversChart(HttpServletResponse response, @PathVariable("date") String date,
			@PathVariable("limit") int limit) {
		response.setContentType("image/png");

		long d = 0L;
		String label = null;
		if (date != null) {
			d = TimeDateUtils.fromString(date);
			label = String.format("OGN Top %d ranges (%s)", limit, date);
		} else {
			label = String.format("OGN Top %d ranges", limit);
		}

		final List<Map<String, Object>> topRangeList =
				d > 0 ? service.getTopMaxRanges(d, limit) : service.getTopMaxRanges(limit);

		final CategoryDataset dataset =
				ChartUtils.createCategoryDataset(topRangeList, ChartType.TOP_RECEIVERS_BY_RANGE);
		final JFreeChart chart =
				ChartUtils.createBarChart(dataset, label, new String[]{"Receiver", "max. reception range [km]"});

		int height = 600;

		if (limit > 30) {
			height = limit * 20;
		}

		ChartUtils.drawChart(response, chart, 950, height);
	}

	@RequestMapping(value = "/toprec-count/{limit}", method = RequestMethod.GET)
	public void drawTopReceiversReceptionCountChart1(HttpServletResponse response, @PathVariable("limit") int limit) {
		drawTopReceiversReceptionCountChart2(response, null, limit);
	}

	@RequestMapping(value = "/toprec-count/{date}/{limit}", method = RequestMethod.GET)
	public void drawTopReceiversReceptionCountChart2(HttpServletResponse response, @PathVariable String date,
			@PathVariable("limit") int limit) {
		response.setContentType("image/png");

		long d = 0L;
		if (date != null)
			d = TimeDateUtils.fromString(date);

		final List<Map<String, Object>> list =
				d > 0 ? service.getTopReceptionCounters(d, limit) : service.getTopReceptionCounters(limit);

		final CategoryDataset dataset =
				ChartUtils.createCategoryDataset(list, ChartType.TOP_RECEIVERS_BY_NUMBER_OF_RECEPTIONS);

		final JFreeChart chart = ChartUtils.createBarChart(dataset, String.format("OGN Top %d (%s)", limit, date),
				new String[]{"Receiver", "aircraft beacons count"});

		int height = 600;

		if (limit > 30) {
			height = limit * 20;
		}

		ChartUtils.drawChart(response, chart, 950, height);
	}

	@RequestMapping(value = "/topalt/{date}/{limit}", method = RequestMethod.GET)
	private void drawTopAltReceiversChart(HttpServletResponse response, @PathVariable("date") String date,
			@PathVariable("limit") int limit) {
		response.setContentType("image/png");

		final long d = TimeDateUtils.fromString(date);

		final List<Map<String, Object>> list = service.getMaxAlts(d, limit);

		final CategoryDataset dataset =
				ChartUtils.createCategoryDataset(list, ChartType.TOP_RECEIVERS_BY_MAX_RECEPTION_ALT);

		final JFreeChart chart = ChartUtils.createBarChart(dataset, String.format("OGN Top %d altitudes", limit),
				new String[]{"Receiver", "aircraft alt [m]"});

		int height = 600;

		if (limit > 30) {
			height = limit * 20;
		}

		ChartUtils.drawChart(response, chart, 950, height);
	}

}