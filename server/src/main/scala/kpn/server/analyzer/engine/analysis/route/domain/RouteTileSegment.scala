package kpn.server.analyzer.engine.analysis.route.domain

case class RouteTileSegment(
  segmentId: Option[Long],
  segmentElementId: Option[Long],
  lines: Seq[String]
)
