package kpn.planner.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kpn.planner.domain.Route;
import kpn.planner.repository.HikingRepository;
import kpn.planner.service.HikingService;

@Service
public class HikingServiceImpl extends AbstractGraphService implements HikingService {

	@Autowired
	public HikingServiceImpl(HikingRepository hikingRepository) {
		super(hikingRepository);
	}

	public Route calculateShortestPath(List<Long> nodes) {
		return calculateShortestRoute(nodes);
	}

	@Override
	public List<Long> calculateShortestPathFromMultiline(String routeId, Long nodeId) {
		return calculateShortestRoute(routeId, nodeId);
	}
}
