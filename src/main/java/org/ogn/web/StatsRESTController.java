package org.ogn.web;

import java.util.List;
import java.util.Map;

import org.ogn.gateway.plugin.stats.TimeDateUtils;
import org.ogn.gateway.plugin.stats.dao.StatsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class StatsRESTController {

	private StatsDAO dao;

	@Autowired
	public void setDao(StatsDAO dao) {
		this.dao = dao;
	}

	@RequestMapping(value = "/activerec/{days}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonActiveReceivers(@PathVariable("days") String days) {
		if (0 == days.length())
			return dao.getActiveReceiversCounters(0);
		else
			return dao.getActiveReceiversCounters(Integer.parseInt(days));
	}

	@RequestMapping(value = "/toprange/{count}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(@PathVariable("count") int count) {
		return dao.getTopMaxRanges(count);
	}

	@RequestMapping(value = "/toprange-all/{date}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(@PathVariable("date") String date) {
		return jsonTopRangeReceivers(date, 0);
	}

	@RequestMapping(value = "/toprange/{date}/{count}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(@PathVariable("date") String date,
			@PathVariable("count") int count) {
		long d = TimeDateUtils.fromString(date);
		return dao.getTopMaxRanges(d, count);
	}

	@RequestMapping(value = "/topcount/{date}/{count}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopCountReceivers(@PathVariable("date") String date,
			@PathVariable("count") int count) {
		long d = TimeDateUtils.fromString(date);
		return dao.getTopReceptionCounters(d, count);
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
	private List<Map<String, Object>> jsonTopAltReceivers(@PathVariable("date") String date,
			@PathVariable("limit") int limit) {
		long d = TimeDateUtils.fromString(date);
		return dao.getMaxAlts(d, limit);
	}

}