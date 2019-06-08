package kpn.planner.repository;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kpn.planner.domain.document.Document;
import kpn.planner.domain.document.JsonNodeMapper;
import kpn.planner.util.Databases;

@Repository
public class CyclingRepository implements CouchDbRepository {

	private final Databases databases;

	@Autowired
	public CyclingRepository(Databases databases) {
		this.databases = databases;
	}

	public Document getRouteDocumentById(String routeId) {
		CouchDbConnector connector = databases.getMain();
		return connector.get(Document.class, "route:" + routeId);
	}

	public List<JsonNodeMapper> getNodesFromFiles() {
		CouchDbConnector connector = databases.getMain();

		ViewQuery query = new ViewQuery();
		query.designDocId("_design/PlannerDesign");
		query.viewName("GraphEdgesView");
		query.cacheOk(true);
		query.queryParam("reduce", "false");
		query.queryParam("startkey", "[\"rcn\"]");
		query.queryParam("endkey", "[\"rcn\",{}]");

		ViewResult result = connector.queryView(query);

		return parseViewResult(result);
	}
}
