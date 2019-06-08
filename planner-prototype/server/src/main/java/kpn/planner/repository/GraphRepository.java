package kpn.planner.repository;

import java.util.List;

import kpn.planner.domain.NetworkType;
import kpn.planner.domain.document.JsonNodeMapper;
import kpn.planner.domain.document.RouteDocument;

public interface GraphRepository {

	RouteDocument getRouteDocumentById(String routeId);

	List<JsonNodeMapper> getNodesFromFiles(NetworkType networkType);
}
