package kpn.api.common.route

import kpn.api.common.data.MemberType

case class RouteStructureRow(
  id: Long,
  memberType: MemberType,
  role: String,
  linkName: String,
  way: Option[RouteStructureWay] = None,
  relation: Option[RouteStructureRelation] = None,
)
