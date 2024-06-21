package kpn.server.analyzer.engine.analysis.route

import kpn.server.analyzer.engine.analysis.route.structure.Structure

case class RouteSegmentAnalysis(
  osmDistance: Long,
  routeSegments: Seq[RouteSegmentData],
  structure: Structure
) {
  def startNodeId: Option[Long] = routeSegments.headOption.map(_.segment.startNodeId)

  def endNodeId: Option[Long] = routeSegments.lastOption.map(_.segment.endNodeId)
}
