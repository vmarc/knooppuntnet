package kpn.server.analyzer.engine.analysis.route

case class RouteSegmentAnalysis(
  osmDistance: Long,
  routeSegments: Seq[RouteSegmentData]
) {
  def startNodeId: Option[Long] = routeSegments.headOption.map(_.segment.startNodeId)

  def endNodeId: Option[Long] = routeSegments.lastOption.map(_.segment.endNodeId)
}
