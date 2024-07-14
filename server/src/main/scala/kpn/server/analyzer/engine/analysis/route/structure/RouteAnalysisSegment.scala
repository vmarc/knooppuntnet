package kpn.server.analyzer.engine.analysis.route.structure

case class RouteAnalysisSegment(
  id: Long,
  fromNodeId: Long,
  toNodeId: Long,
  elements: Seq[RouteAnalysisElement]
)
