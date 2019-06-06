package com.knooppuntnet.controller;

import com.knooppuntnet.domain.Route;
import com.knooppuntnet.service.HikingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hiking")
public class HikingController {

	private final HikingService hikingService;

	public HikingController(HikingService hikingService) {
		this.hikingService = hikingService;
	}

	@GetMapping("/route")
	public Route calculateRoute(@RequestParam("nodes") List<Long> nodes) {
		return this.hikingService.calculateShortestPath(nodes);
	}

	@GetMapping("/{routeId}/{nodeId}")
	public List<Long> calculateShortestPathFromMultiline(@PathVariable("routeId") String routeId, @PathVariable("nodeId") Long nodeId) {
		return this.hikingService.calculateShortestPathFromMultiline(routeId, nodeId);
	}
}
