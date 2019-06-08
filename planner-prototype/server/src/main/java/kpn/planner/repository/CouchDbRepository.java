package kpn.planner.repository;

import java.util.ArrayList;
import java.util.List;

import org.ektorp.ViewResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kpn.planner.domain.document.Document;
import kpn.planner.domain.document.JsonNodeMapper;

public interface CouchDbRepository {

	List<JsonNodeMapper> getNodesFromFiles();

	Document getRouteDocumentById(String routeId);

	default List<JsonNodeMapper> parseViewResult(ViewResult result) {
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
