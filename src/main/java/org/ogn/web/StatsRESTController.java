package org.ogn.web;

import java.util.List;
import java.util.Map;

import org.ogn.gateway.plugin.stats.TimeDateUtils;
import org.ogn.gateway.plugin.stats.dao.StatsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
	public List<Map<String, Object>> jsonActiveReceivers(Model model, @PathVariable("days") int days) {
		return dao.getActiveReceiversCount(days);
	}

	@RequestMapping(value = "/activerec-all", method = RequestMethod.GET)
	public  List<Map<String, Object>> jsonActiveReceivers(Model model) {
		return jsonActiveReceivers(model,0);
	}

	@RequestMapping(value = "/toprange/{count}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(Model model, @PathVariable("count") int count) {
		return dao.getTopRangeRecords(count);
	}

	@RequestMapping(value = "/toprange-all/{date}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(Model model, @PathVariable("date") String date) {
		return jsonTopRangeReceivers(model, date, 0);
	}

	@RequestMapping(value = "/toprange/{date}/{count}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(Model model, @PathVariable("date") String date,
			@PathVariable("count") int count) {
		long d = TimeDateUtils.fromString(date);		
		return dao.getTopRangeRecords(d, count);
	}
	
	
	@RequestMapping(value = "/topcount/{date}/{count}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopCountReceivers(Model model, @PathVariable("date") String date,
			@PathVariable("count") int count) {
		long d = TimeDateUtils.fromString(date);		
		return dao.getTopCountRecords(d, count);
	}
	
	@RequestMapping(value = "/topcount-all/{date}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopCountReceivers(Model model, @PathVariable("date") String date) {
		return jsonTopCountReceivers(model,date,0);
	}

}