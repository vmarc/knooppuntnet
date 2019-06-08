package kpn.planner.repository;

import java.util.ArrayList;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kpn.planner.domain.NetworkType;
import kpn.planner.domain.document.JsonNodeMapper;
import kpn.planner.domain.document.RouteDocument;
import kpn.planner.util.Databases;

@Repository
public class GraphRepositoryImpl implements GraphRepository {

	private static final Logger logger = LoggerFactory.getLogger(GraphRepositoryImpl.class);

	private final CouchDbConnector database;

	public GraphRepositoryImpl(Databases databases) {
		this.database = databases.getMain();
	}

	public RouteDocument getRouteDocumentById(String routeId) {
		return database.get(RouteDocument.class, "route:" + routeId);
	}

	public List<JsonNodeMapper> getNodesFromFiles(NetworkType networkType) {

		String startkey = String.format("[\"%s\"]", networkType.getTagKey());
		String endkey = String.format("[\"%s\",{}]", networkType.getTagKey());

		ViewQuery query = new ViewQuery();
		query.designDocId("_design/PlannerDesign");
		query.viewName("GraphEdgesView");
		query.cacheOk(true);
		query.queryParam("reduce", "false");
		query.queryParam("stale", "ok");
		query.queryParam("startkey", startkey);
		query.queryParam("endkey", endkey);

		ViewResult result = database.queryView(query);

		logger.info("Loaded graph edges " + networkType.name() + ": " + result.getSize());

		return parseViewResult(result);
	}

	private List<JsonNodeMapper> parseViewResult(ViewResult result) {
		List<JsonNodeMapper> nodeList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();

		result.getRows().forEach(row -> {
			JsonNodeMapper nodeMapper = new JsonNodeMapper();
			nodeMapper.setKey(objectMapper.convertValue(row.getKeyAsNode(), new TypeReference<ArrayList<String>>() {
			}));
			nodeMapper.setValues(objectMapper.convertValue(row.getValueAsNode(), new TypeReference<ArrayList<Long>>() {
			}));
			nodeList.add(nodeMapper);
		});

		return nodeList;
	}
}
