package kpn.planner.repository;

import org.ektorp.CouchDbConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kpn.planner.domain.poi.Poi;
import kpn.planner.domain.poi.PoiElement;
import kpn.planner.util.Databases;

@Repository
public class PoiRepository {

	private Databases databases;

	@Autowired
	public PoiRepository(Databases databases) {
		this.databases = databases;
	}

	public Poi getPoiInformation(String type, String poiId) {
		CouchDbConnector connector = databases.getPois();
		return connector.get(PoiElement.class, "poi:" + type + ":" + poiId).getPoi();
	}
}
