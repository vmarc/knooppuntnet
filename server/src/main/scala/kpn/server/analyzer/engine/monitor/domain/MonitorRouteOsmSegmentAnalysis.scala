package kpn.server.analyzer.engine.monitor.domain

case class MonitorRouteOsmSegmentAnalysis(
  osmDistance: Long,
  routeSegments: Seq[MonitorRouteSegmentData]
) {
  def startNodeId = routeSegments.headOption.map(_.segment.startNodeId)

  def endNodeId = routeSegments.lastOption.map(_.segment.endNodeId)

}
