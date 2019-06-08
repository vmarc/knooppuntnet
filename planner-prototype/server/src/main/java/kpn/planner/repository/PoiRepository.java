package kpn.planner.repository;

import org.ektorp.CouchDbConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kpn.planner.domain.poi.Poi;
import kpn.planner.domain.poi.PoiElement;
import kpn.planner.util.ConnectorProducer;

@Repository
public class PoiRepository {

	@Autowired
	private ConnectorProducer connectorProducer;

	public Poi getPoiInformation(String type, String poiId) {
		CouchDbConnector connector = connectorProducer.getCouchDbConnectorPoi();
		return connector.get(PoiElement.class, "poi:" + type + ":" + poiId).getPoi();
	}
}
