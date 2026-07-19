package kpn.api.common;

import kpn.api.common.PoiAnalysis;
import kpn.api.common.PoiState;
import kpn.api.common.poi.Poi;

public record PoiDetail(
  Poi poi,
  PoiAnalysis poiAnalysis,
  PoiState poiState
) {
}

/*
package kpn.api.common

import kpn.api.common.poi.Poi

case class PoiDetail(
  poi: Poi,
  poiAnalysis: PoiAnalysis,
  poiState: PoiState
)

*/
