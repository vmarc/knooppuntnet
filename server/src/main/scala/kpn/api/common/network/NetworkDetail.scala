package kpn.api.common.network

import kpn.api.common.LatLonImpl
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class NetworkDetail(
  km: Long,
  meters: Long,
  lastUpdated: Timestamp,
  relationLastUpdated: Timestamp,
  lastSurvey: Option[Day],
  brokenRouteCount: Long,
  brokenRoutePercentage: String,
  integrity: Integrity,
  inaccessibleRouteCount: Long,
  connectionCount: Long,
  center: Option[LatLonImpl],
)
