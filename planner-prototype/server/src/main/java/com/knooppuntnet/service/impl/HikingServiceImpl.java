package com.knooppuntnet.service.impl;

import com.knooppuntnet.domain.Route;
import com.knooppuntnet.repository.HikingRepository;
import com.knooppuntnet.service.HikingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
