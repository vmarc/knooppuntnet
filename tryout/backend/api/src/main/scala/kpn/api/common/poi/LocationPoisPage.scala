package kpn.api.common.poi

case class LocationPoisPage(
  poiCount: Long,
  pois: Seq[LocationPoiInfo]
)
