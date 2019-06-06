package com.knooppuntnet.controller;

import com.knooppuntnet.domain.Route;
import com.knooppuntnet.service.CyclingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cycling")
public class CyclingController {

	private final CyclingService cyclingService;

	@Autowired
	public CyclingController(CyclingService cyclingService) {
		this.cyclingService = cyclingService;
	}

	@GetMapping("/route")
	public Route calculateRoute(@RequestParam("nodes") List<Long> nodes) {
		return this.cyclingService.calculateShortestPath(nodes);
	}

	@GetMapping("/{routeId}/{nodeId}")
	public List<Long> calculateShortestPathFromMultiline(@PathVariable("routeId") String routeId, @PathVariable("nodeId") Long nodeId) {
		return this.cyclingService.calculateShortestPathFromMultiline(routeId, nodeId);
	}
}
