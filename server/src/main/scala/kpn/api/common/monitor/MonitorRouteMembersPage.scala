package kpn.api.common.monitor

import kpn.api.common.Bounds
import kpn.api.common.RouteType
import kpn.api.common.route.StructureRow

case class MonitorRouteMembersPage(
  adminRole: Boolean,
  groupName: String,
  groupDescription: String,
  routeName: String,
  routeDescription: String,
  routeId: String,
  relationId: Option[Long],
  relationIds: Seq[Long],
  referenceType: MonitorReferenceType,
  routeTypes: Seq[RouteType],
  structureRows: Seq[StructureRow],
  bounds: Option[Bounds]
)
