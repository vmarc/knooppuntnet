package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.id.Storable

case class RouteTileSegment(
  segmentId: Option[Long],
  segmentElementId: Option[Long],
  lines: Seq[String]
) extends Storable
