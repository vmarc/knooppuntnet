package kpn.api.common

case class PoiPage(
  elementType: String,
  elementId: Long,
  latitude: String,
  longitude: String,
  analysis: PoiAnalysis
)
