package kpn.planner.repository;

import kpn.planner.domain.poi.Poi;

public interface PoiRepository {

	Poi getPoi(String type, String poiId);
}
