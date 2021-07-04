package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.common.TrackPathKey
import kpn.api.common.route.RouteMap
import kpn.core.planner.graph.GraphEdge

class RouteGraphEdgesBuilder {

  def build(routeId: Long, routeMap: RouteMap, proposed: Boolean): Seq[GraphEdge] = {
    routeMap.paths.map { trackPath =>
      GraphEdge(
        trackPath.startNodeId,
        trackPath.endNodeId,
        trackPath.meters,
        proposed,
        TrackPathKey(routeId, trackPath.pathId)
      )
    }
  }
}
