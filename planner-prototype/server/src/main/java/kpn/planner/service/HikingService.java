package kpn.planner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kpn.planner.domain.Route;

@Service
public interface HikingService {

	Route calculateShortestPath(List<Long> nodes);

	List<Long> calculateShortestPathFromMultiline(String routeId, Long nodeId);
}
