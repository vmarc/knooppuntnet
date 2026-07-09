package kpn.api.common.location

import kpn.api.common.Fact
import kpn.api.common.LatLon
import kpn.api.common.common.Reference
import kpn.api.custom.Day
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
  expectedRouteCount: Option[Long],
  routeReferences: Seq[Reference]
) extends LatLon
