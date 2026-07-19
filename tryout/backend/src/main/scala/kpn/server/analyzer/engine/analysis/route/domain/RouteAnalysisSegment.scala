package kpn.server.analyzer.engine.analysis.route.domain

case class RouteAnalysisSegment(
  id: Long,
  fromNodeId: Long,
  toNodeId: Long,
  elements: Seq[RouteAnalysisElement]
)
