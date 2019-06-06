package com.knooppuntnet.repository;

import com.knooppuntnet.domain.document.JsonNodeMapper;
import com.knooppuntnet.util.ConnectorProducer;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HikingRepository implements CouchDbRepository {

	public List<JsonNodeMapper> getNodesFromFiles() {
		CouchDbConnector connector = ConnectorProducer.getCouchDbConnectorKnooppunt();

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
