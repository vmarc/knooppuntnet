package kpn.server.analyzer.engine.analysis.route.structure

case class NewRouteSegment(
  id: Long,
  fromNodeId: Long,
  toNodeId: Long,
  elements: Seq[NewRouteSegmentElement]
)
