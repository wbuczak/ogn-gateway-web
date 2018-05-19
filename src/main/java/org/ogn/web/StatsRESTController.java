package org.ogn.web;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ogn.commons.igc.IgcUrlCache;
import org.ogn.gateway.plugin.stats.TimeDateUtils;
import org.ogn.gateway.plugin.stats.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class StatsRESTController {

	@Value("${OGN_GATEWAY_IGC_BASE_URL:http://ognstats.ddns.net/igc}")
	private String			igcBaseUrl;

	private StatsService	service;

	private IgcUrlCache		igcUrlCache;

	@PostConstruct
	public void init() {
		igcUrlCache = new IgcUrlCache(igcBaseUrl);
	}

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

	@RequestMapping(value = "/toprange/{limit}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(@PathVariable("limit") int limit) {
		return service.getTopMaxRanges(limit);
	}

	@RequestMapping(value = "/toprange-all/{date}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(@PathVariable("date") String date) {
		return enrichEntriesWithIgcUrls(date, jsonTopRangeReceivers(date, 0));
	}

	@RequestMapping(value = "/toprange/{date}/{limit}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopRangeReceivers(@PathVariable("date") String date,
			@PathVariable("limit") int limit) {
		final long d = TimeDateUtils.fromString(date);

		// enrich with igc file urls
		return enrichEntriesWithIgcUrls(date, service.getTopMaxRanges(d, limit));
	}

	@RequestMapping(value = "/topcount/{date}/{limit}", method = RequestMethod.GET)
	public List<Map<String, Object>> jsonTopCountReceivers(@PathVariable("date") String date,
			@PathVariable("limit") int limit) {
		final long d = TimeDateUtils.fromString(date);
		return service.getTopReceptionCounters(d, limit);
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
		final long d = TimeDateUtils.fromString(date);
		return enrichEntriesWithIgcUrls(date, service.getMaxAlts(d, limit));

		// return service.getMaxAlts(d, limit);
	}

	private List<Map<String, Object>> enrichEntriesWithIgcUrls(String date, List<Map<String, Object>> entries) {
		entries.parallelStream().forEach(map -> map.put("igc_url",
				igcUrlCache.getIgcFileUrl(date, ((String) map.get("aircraft_id")).substring(3)).orElse(null)));
		return entries;
	}

}