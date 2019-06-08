package com.knooppuntnet.repository;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.knooppuntnet.domain.document.Document;
import com.knooppuntnet.domain.document.JsonNodeMapper;
import com.knooppuntnet.util.ConnectorProducer;

@Repository
public class HikingRepository implements CouchDbRepository {

	private final ConnectorProducer connectorProducer;

	@Autowired
	public HikingRepository(ConnectorProducer connectorProducer) {
		this.connectorProducer = connectorProducer;
	}


	public Document getRouteDocumentById(String routeId) {
		CouchDbConnector connector = connectorProducer.getCouchDbConnectorKnooppunt();
		return connector.get(Document.class, "route:" + routeId);
	}

	public List<JsonNodeMapper> getNodesFromFiles() {
		CouchDbConnector connector = connectorProducer.getCouchDbConnectorKnooppunt();

		ViewQuery query = new ViewQuery();
		query.designDocId("_design/PlannerDesign");
		query.viewName("GraphEdgesView");
		query.cacheOk(true);
		query.queryParam("reduce", "false");
		query.queryParam("startkey", "[\"rwn\"]");
		query.queryParam("endkey", "[\"rwn\",{}]");

		ViewResult result = connector.queryView(query);

		return parseViewResult(result);
	}
}
