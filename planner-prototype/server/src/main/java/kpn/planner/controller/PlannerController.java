package kpn.planner.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kpn.planner.domain.NetworkType;
import kpn.planner.domain.Route;
import kpn.planner.service.PlannerService;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {

	private final PlannerService plannerService;

	public PlannerController(PlannerService plannerService) {
		this.plannerService = plannerService;
	}

	@GetMapping("/{networkType}/route")
	public Route calculateRoute(@PathVariable("networkType") NetworkType networkType, @RequestParam("nodes") List<Long> nodes) {
		return this.plannerService.calculateShortestPath(networkType, nodes);
	}

	@GetMapping("/{networkType}/{routeId}/{nodeId}")
	public List<Long> calculateShortestPathFromMultiline(@PathVariable("networkType") NetworkType networkType, @PathVariable("routeId") String routeId,
			@PathVariable("nodeId") Long nodeId) {
		return this.plannerService.calculateShortestPathFromMultiline(networkType, routeId, nodeId);
	}
}
