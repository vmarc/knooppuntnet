package kpn.api.common

import kpn.api.common.data.Tagable
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class RouteSummary(
  id: Long,
  countries: Seq[Country],
  nodeNetwork: Boolean,
  networkTypes: Seq[NetworkType],
  // TODO redesign - reintroduce networkScope: NetworkScope, ?
  name: String,
  meters: Long,
  broken: Boolean,
  inaccessible: Boolean,
  wayCount: Long,
  timestamp: Timestamp, // TODO redesign - same as RouteDoc.lastUpdated ???
  tags: Seq[Tag]
) extends Tagable
