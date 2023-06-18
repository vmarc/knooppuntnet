package kpn.api.common

import kpn.api.common.poi.Poi

case class PoiDetail(
  poi: Poi,
  poiAnalysis: PoiAnalysis,
  poiState: PoiState
)
