package kpn.api.common.location

import kpn.api.common.LatLon
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.Timestamp

case class LocationNodeInfo(
  rowIndex: Long,
  id: Long,
  name: String,
  longName: String,
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  facts: Seq[Fact],
  expectedRouteCount: String,
  routeReferences: Seq[Reference]
) extends LatLon
