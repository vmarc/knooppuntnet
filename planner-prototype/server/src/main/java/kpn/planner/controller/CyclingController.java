package kpn.planner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kpn.planner.domain.Route;
import kpn.planner.service.CyclingService;

@RestController
@RequestMapping("/api/cycling")
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
