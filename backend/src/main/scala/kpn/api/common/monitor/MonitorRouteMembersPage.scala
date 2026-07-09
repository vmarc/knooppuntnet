package kpn.api.common.monitor

import kpn.api.common.RouteType
import kpn.api.common.route.StructureRow

case class MonitorRouteMembersPage(
  summary: MonitorRouteSummary,
  referenceType: MonitorReferenceType,
  routeTypes: Seq[RouteType],
  structureRows: Seq[StructureRow]
)
