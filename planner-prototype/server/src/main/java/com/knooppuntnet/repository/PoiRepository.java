package com.knooppuntnet.repository;

import com.knooppuntnet.domain.poi.Poi;
import com.knooppuntnet.domain.poi.PoiElement;
import com.knooppuntnet.util.ConnectorProducer;
import org.ektorp.CouchDbConnector;
import org.springframework.stereotype.Repository;

@Repository
public class PoiRepository {

	public Poi getPoiInformation(String type, String poiId) {
		CouchDbConnector connector = ConnectorProducer.getCouchDbConnectorPoi();
		return connector.get(PoiElement.class, "poi:" + type + ":" + poiId).getPoi();
	}
}
