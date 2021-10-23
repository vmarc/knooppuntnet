package kpn.api.common.network

import kpn.api.common.LatLonImpl
import kpn.api.common.data.MetaData
import kpn.api.custom.Day
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class NetworkDetail(
  km: Long,
  meters: Long,
  version: Long,
  changeSetId: Long,
  lastUpdated: Timestamp,
  relationLastUpdated: Timestamp,
  lastSurvey: Option[Day],
  tags: Tags,
  brokenRouteCount: Long,
  brokenRoutePercentage: String,
  integrity: Integrity,
  inaccessibleRouteCount: Long,
  connectionCount: Long,
  center: Option[LatLonImpl],
) {
  def toMeta: MetaData = {
    MetaData(
      version,
      relationLastUpdated,
      changeSetId
    )
  }
}
