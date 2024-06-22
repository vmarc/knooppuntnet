package kpn.server.analyzer.engine.analysis.route

import kpn.server.analyzer.engine.analysis.route.structure.Structure
import kpn.server.analyzer.engine.analysis.route.structure.StructureElementGroup

case class RouteSegmentAnalysis(
  osmDistance: Long,
  elementGroups: Seq[StructureElementGroup],
  routeSegments: Seq[RouteSegmentData],
  structure: Structure
) {
  def startNodeId: Option[Long] = routeSegments.headOption.map(_.segment.startNodeId)

  def endNodeId: Option[Long] = routeSegments.lastOption.map(_.segment.endNodeId)
}
