package kpn.planner.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kpn.planner.domain.Route;
import kpn.planner.service.HikingService;

@RestController
@RequestMapping("/api/hiking")
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
