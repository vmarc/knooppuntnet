package kpn.api.common.route

import kpn.api.custom.Day
import kpn.core.doc.BaseRouteSegment

case class RouteStructureRelation(
  level: Long,
  physical: Boolean,
  name: String,
  subRelationIndex: Option[Long],
  survey: Option[Day],
  symbol: Option[String],
  segments: Seq[BaseRouteSegment],
  totalDistance: Long,
  gaps: Option[String],
  happy: Boolean
)
