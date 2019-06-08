package kpn.planner.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kpn.planner.domain.Route;
import kpn.planner.service.GpxService;

@RestController
@RequestMapping("/api/gpx")
public class GpxController {

	private final GpxService gpxService;

	public GpxController(GpxService gpxService) {
		this.gpxService = gpxService;
	}

	@PostMapping
	public void createGpxFile(@RequestBody Route route, HttpServletResponse response) {
		this.gpxService.createGpx(route, response);
	}
}
