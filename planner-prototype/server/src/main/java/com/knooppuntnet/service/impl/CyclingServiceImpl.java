package com.knooppuntnet.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.knooppuntnet.domain.Route;
import com.knooppuntnet.repository.CyclingRepository;
import com.knooppuntnet.service.CyclingService;

@Service
public class CyclingServiceImpl extends AbstractGraphService implements CyclingService {

	@Autowired
	public CyclingServiceImpl(CyclingRepository cyclingRepository) {
		super(cyclingRepository);
	}

	public Route calculateShortestPath(List<Long> nodes) {
		return calculateShortestRoute(nodes);
	}

	@Override
	public List<Long> calculateShortestPathFromMultiline(String routeId, Long nodeId) {
		return calculateShortestRoute(routeId, nodeId);
	}
}
