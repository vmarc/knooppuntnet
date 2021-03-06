package kpn.api.common.location

import kpn.api.common.LatLon
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class LocationNodeInfo(
  id: Long,
  name: String,
  longName: String,
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  factCount: Int,
  expectedRouteCount: String,
  routeReferences: Seq[Reference]
) extends LatLon
