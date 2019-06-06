package com.knooppuntnet.service;

import com.knooppuntnet.domain.Route;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HikingService {

	Route calculateShortestPath(List<Long> nodes);

	List<Long> calculateShortestPathFromMultiline(String routeId, Long nodeId);
}
