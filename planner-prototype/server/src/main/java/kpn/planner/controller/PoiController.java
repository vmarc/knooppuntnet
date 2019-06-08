package kpn.planner.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kpn.planner.domain.poi.Poi;
import kpn.planner.service.PoiService;

@RestController
@RequestMapping("/api/poi")
public class PoiController {

	private final PoiService poiService;

	public PoiController(PoiService poiService) {
		this.poiService = poiService;
	}

	@GetMapping("/{type}/{poiId}")
	public Poi getPoiInformation(@PathVariable("type") String type, @PathVariable("poiId") String poiId) {
		return this.poiService.getPoiInformation(type, poiId);
	}
}
