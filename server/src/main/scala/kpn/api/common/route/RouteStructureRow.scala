package kpn.api.common.route

import kpn.api.common.data.MemberType

case class RouteStructureRow(
  rowNumber: String,
  id: Long,
  memberType: MemberType,
  role: Option[String],
  link: Option[Link],
  distance: Long,
  name: Option[String],
  poi: Option[String],
  way: Option[RouteStructureWay] = None,
  relation: Option[RouteStructureRelation] = None,
  segmentIds: Seq[Long] = Seq.empty,
  pathIds: Seq[Long] = Seq.empty,
)
