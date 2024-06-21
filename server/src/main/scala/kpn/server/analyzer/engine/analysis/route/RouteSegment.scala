package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.Bounds

case class RouteSegment(
  id: Long,
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  bounds: Bounds,
  geoJson: String
)
