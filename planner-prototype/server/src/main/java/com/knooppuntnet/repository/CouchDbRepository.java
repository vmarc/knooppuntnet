package com.knooppuntnet.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knooppuntnet.domain.document.Document;
import com.knooppuntnet.domain.document.JsonNodeMapper;
import com.knooppuntnet.util.ConnectorProducer;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewResult;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CouchDbRepository {

	List<JsonNodeMapper> getNodesFromFiles();

	default Document getRouteDocumentById(String routeId) {
			CouchDbConnector connector = ConnectorProducer.getCouchDbConnectorKnooppunt();
			return connector.get(Document.class, "route:" + routeId);
	}

	default List<JsonNodeMapper> parseViewResult(ViewResult result) {
		List<JsonNodeMapper> nodeList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();

		result.getRows().forEach(row -> {
			JsonNodeMapper nodeMapper = new JsonNodeMapper();
			nodeMapper.setKey(objectMapper.convertValue(row.getKeyAsNode(), new TypeReference<ArrayList<String>>() {}));
			nodeMapper.setValues(objectMapper.convertValue(row.getValueAsNode(), new TypeReference<ArrayList<Long>>() {}));
			nodeList.add(nodeMapper);
		});

		return nodeList;
	}
}
