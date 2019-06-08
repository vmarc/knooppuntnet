package kpn.planner.service;

import java.util.List;

import kpn.planner.domain.NetworkType;
import kpn.planner.domain.Route;

public interface PlannerService {

	Route calculateShortestPath(NetworkType networkType, List<Long> nodes);

	List<Long> calculateShortestPathFromMultiline(NetworkType networkType, String routeId, Long nodeId);
}
