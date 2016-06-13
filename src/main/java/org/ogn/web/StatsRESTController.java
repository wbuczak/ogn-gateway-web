package org.ogn.web;

import java.util.List;
import java.util.Map;

import org.ogn.gateway.plugin.stats.TimeDateUtils;
import org.ogn.gateway.plugin.stats.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class StatsRESTController {

	private StatsService service;

	@Autowired
	public void setReceiversService(StatsService service) {
		this.service = service;
	}

	@RequestMapping(value = "/activerec/{days}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonActiveReceivers(@PathVariable("days") String days) {
		if (0 == days.length())
			return service.getDailyStatsForDays(0);
		else
			return service.getDailyStatsForDays(Integer.parseInt(days));
	}

	@RequestMapping(value = "/toprange/{count}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(@PathVariable("count") int count) {
		return service.getTopMaxRanges(count);
	}

	@RequestMapping(value = "/toprange-all/{date}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(@PathVariable("date") String date) {
		return jsonTopRangeReceivers(date, 0);
	}

	@RequestMapping(value = "/toprange/{date}/{count}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(@PathVariable("date") String date,
			@PathVariable("count") int count) {
		long d = TimeDateUtils.fromString(date);
		return service.getTopMaxRanges(d, count);
	}

	@RequestMapping(value = "/topcount/{date}/{count}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopCountReceivers(@PathVariable("date") String date,
			@PathVariable("count") int count) {
		long d = TimeDateUtils.fromString(date);
		return service.getTopReceptionCounters(d, count);
	}

	@RequestMapping(value = "/topcount-all/{date}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopCountReceivers(@PathVariable("date") String date) {
		return jsonTopCountReceivers(date, 0);
	}

	@RequestMapping(value = "/topalt-all/{date}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopAltReceivers(@PathVariable("date") String date) {
		return jsonTopAltReceivers(date, 0);
	}

	@RequestMapping(value = "/topalt-all/{date}/{limit}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopAltReceivers(@PathVariable("date") String date,
			@PathVariable("limit") int limit) {
		long d = TimeDateUtils.fromString(date);
		return service.getMaxAlts(d, limit);
	}

}