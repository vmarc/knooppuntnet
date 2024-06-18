package kpn.server.analyzer.engine.monitor.domain

case class MonitorRouteOsmSegmentAnalysis(
  osmDistance: Long,
  routeSegments: Seq[MonitorRouteSegmentData]
) {
  def startNodeId: Option[Long] = routeSegments.headOption.map(_.segment.startNodeId)

  def endNodeId: Option[Long] = routeSegments.lastOption.map(_.segment.endNodeId)
}
