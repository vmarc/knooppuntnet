package kpn.api.common.monitor;

import kpn.api.common.RouteType;
import kpn.api.common.monitor.MonitorReferenceType;
import kpn.api.common.monitor.MonitorRouteSummary;
import kpn.api.common.route.StructureRow;

import com.google.common.collect.ImmutableList;

public record MonitorRouteMembersPage(
  MonitorRouteSummary summary,
  MonitorReferenceType referenceType,
  ImmutableList<RouteType> routeTypes,
  ImmutableList<StructureRow> structureRows
) {
}

/*
package kpn.api.common.monitor

import kpn.api.common.RouteType
import kpn.api.common.route.StructureRow

case class MonitorRouteMembersPage(
  summary: MonitorRouteSummary,
  referenceType: MonitorReferenceType,
  routeTypes: Seq[RouteType],
  structureRows: Seq[StructureRow]
)

*/
