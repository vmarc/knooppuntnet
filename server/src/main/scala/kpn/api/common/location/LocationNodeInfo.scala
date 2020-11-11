package kpn.api.common.location

import kpn.api.common.LatLon
import kpn.api.common.common.Ref
import kpn.api.custom.Timestamp

case class LocationNodeInfo(
  id: Long,
  name: String,
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  factCount: Int,
  expectedRouteCount: Int,
  routeReferences: Seq[Ref]
) extends LatLon
