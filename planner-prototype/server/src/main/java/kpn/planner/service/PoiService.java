package kpn.planner.service;

import org.springframework.stereotype.Service;

import kpn.planner.domain.poi.Poi;

@Service
public interface PoiService {

	Poi getPoi(String type, String poiId);
}
