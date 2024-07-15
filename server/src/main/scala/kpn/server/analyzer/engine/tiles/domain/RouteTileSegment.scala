package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.LineSegment

case class RouteTileSegment(
  segmentId: Long,
  segmentElementId: Long,
  pathIds: Seq[Long],
  oneWay: Boolean,
  surface: String,
  lineSegments: Seq[LineSegment]
)
