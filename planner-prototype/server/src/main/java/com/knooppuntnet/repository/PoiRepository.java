package com.knooppuntnet.repository;

import org.ektorp.CouchDbConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.knooppuntnet.domain.poi.Poi;
import com.knooppuntnet.domain.poi.PoiElement;
import com.knooppuntnet.util.ConnectorProducer;

@Repository
public class PoiRepository {

	@Autowired
	private ConnectorProducer connectorProducer;

	public Poi getPoiInformation(String type, String poiId) {
		CouchDbConnector connector = connectorProducer.getCouchDbConnectorPoi();
		return connector.get(PoiElement.class, "poi:" + type + ":" + poiId).getPoi();
	}
}
