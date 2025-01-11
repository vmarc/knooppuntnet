package kpn.api.common

import kpn.api.common.data.Tagable
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class RouteSummary(
  id: Long,
  countries: Seq[Country],
  nodeNetwork: Boolean,
  routeTypes: Seq[RouteType],
  scopes: Seq[RouteScope],
  // TODO redesign - reintroduce routeScope: RouteScope, ?
  name: String,
  meters: Long,
  broken: Boolean,
  inaccessible: Boolean,
  wayCount: Long,
  timestamp: Timestamp, // TODO redesign - same as RouteDoc.lastUpdated ???
  tags: Seq[Tag]
) extends Tagable
