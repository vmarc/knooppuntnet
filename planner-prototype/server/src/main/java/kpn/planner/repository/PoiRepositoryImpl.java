package kpn.planner.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kpn.planner.domain.poi.Poi;
import kpn.planner.domain.poi.PoiElement;
import kpn.planner.util.Databases;

@Repository
public class PoiRepositoryImpl implements PoiRepository {

	private Databases databases;

	@Autowired
	public PoiRepositoryImpl(Databases databases) {
		this.databases = databases;
	}

	public Poi getPoi(String type, String poiId) {
		String id = "poi:" + type + ":" + poiId;
		return databases.getPois().get(PoiElement.class, id).getPoi();
	}
}
