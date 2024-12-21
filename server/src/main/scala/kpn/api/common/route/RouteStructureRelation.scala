package kpn.api.common.route

import kpn.api.custom.Day

case class RouteStructureRelation(
  level: Long,
  physical: Boolean,
  name: String,
  subRelationIndex: Option[Long],
  survey: Option[Day],
  symbol: Option[String],
  osmSegmentCount: Option[Long],
  totalDistance: Long,
  gaps: Option[String],
  happy: Boolean
)
