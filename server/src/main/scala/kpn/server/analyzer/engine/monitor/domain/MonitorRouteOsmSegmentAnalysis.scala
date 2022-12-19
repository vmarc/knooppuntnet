package kpn.server.analyzer.engine.monitor.domain

case class MonitorRouteOsmSegmentAnalysis(
  osmDistance: Long,
  routeSegments: Seq[MonitorRouteSegmentData]
)
