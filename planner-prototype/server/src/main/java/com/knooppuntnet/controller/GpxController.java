package com.knooppuntnet.controller;

import com.knooppuntnet.domain.Route;
import com.knooppuntnet.service.GpxService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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
