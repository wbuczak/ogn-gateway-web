package org.ogn.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.ogn.gateway.plugin.stats.TimeDateUtils;
import org.ogn.gateway.plugin.stats.service.StatsReceiversService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/charts")
public class ChartController {

	private StatsReceiversService rService;

	@Autowired
	public void setReceiversService(StatsReceiversService service) {
		this.rService = service;
	}

	@RequestMapping(value = "/activerec", method = RequestMethod.GET)
	public void drawActiveReceiversChart(HttpServletResponse response) {
		drawActiveReceiversChart(response, 30);
	}

	@RequestMapping(value = "/activerec/{days}", method = RequestMethod.GET)
	public void drawActiveReceiversChart(HttpServletResponse response, @PathVariable("days") int days) {
		response.setContentType("image/png");

		List<Map<String, Object>> activeReceivers = rService.getActiveReceiversCounters(days);

		XYDataset dataset = ChartUtils.createXYDataSet(activeReceivers);
		JFreeChart chart = ChartUtils.createTimeSeriesChart(dataset, "OGN active receivers");

		ChartUtils.drawChart(response, chart, 750, 400);
	}

	@RequestMapping(value = "/toprec-range/{count}", method = RequestMethod.GET)
	public void drawTopRangeReceiversChart(HttpServletResponse response, @PathVariable("count") int count) {
		drawTopRangeReceiversChart(response, null, count);
	}

	@RequestMapping(value = "/toprec-range/{date}/{count}", method = RequestMethod.GET)
	public void drawTopRangeReceiversChart(HttpServletResponse response, @PathVariable("date") String date,
			@PathVariable("count") int count) {
		response.setContentType("image/png");

		long d = 0L;
		String label = null;
		if (date != null) {
			d = TimeDateUtils.fromString(date);
			label = String.format("OGN Top %d ranges (%s)", count, date);
		} else {
			label = String.format("OGN Top %d ranges", count);
		}

		List<Map<String, Object>> topRangeList = d > 0 ? rService.getTopMaxRanges(d, count)
				: rService.getTopMaxRanges(count);

		CategoryDataset dataset = ChartUtils.createCategoryDataset(topRangeList, ChartType.TOP_RECEIVERS_BY_RANGE);
		JFreeChart chart = ChartUtils.createBarChart(dataset, label,
				new String[] { "Receiver", "max. reception range [km]" });

		int height = 600;

		if (count > 30) {
			height = count * 20;
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

		List<Map<String, Object>> list = d > 0 ? rService.getTopReceptionCounters(d, limit)
				: rService.getTopReceptionCounters(limit);

		CategoryDataset dataset = ChartUtils.createCategoryDataset(list,
				ChartType.TOP_RECEIVERS_BY_NUMBER_OF_RECEPTIONS);

		JFreeChart chart = ChartUtils.createBarChart(dataset, String.format("OGN Top %d (%s)", limit, date),
				new String[] { "Receiver", "aircraft beacons count" });

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

		long d = TimeDateUtils.fromString(date);

		List<Map<String, Object>> list = rService.getMaxAlts(d, limit);

		CategoryDataset dataset = ChartUtils.createCategoryDataset(list, ChartType.TOP_RECEIVERS_BY_MAX_RECEPTION_ALT);

		JFreeChart chart = ChartUtils.createBarChart(dataset, String.format("OGN Top %d altitudes", limit),
				new String[] { "Receiver", "aircraft alt [m]" });

		int height = 600;

		if (limit > 30) {
			height = limit * 20;
		}

		ChartUtils.drawChart(response, chart, 950, height);
	}

}