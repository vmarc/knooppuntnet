package kpn.planner.repository;

import java.util.List;

import kpn.planner.domain.NetworkType;
import kpn.planner.domain.document.JsonNodeMapper;
import kpn.planner.domain.document.RouteDoc;

public interface GraphRepository {

	RouteDoc getRouteDocumentById(String routeId);

	List<JsonNodeMapper> getNodesFromFiles(NetworkType networkType);
}
