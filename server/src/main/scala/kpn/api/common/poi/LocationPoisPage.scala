package kpn.api.common.poi

import kpn.api.common.TimeInfo

case class LocationPoisPage(
  timeInfo: TimeInfo,
  poiCount: Long,
  pois: Seq[LocationPoiInfo]
)
