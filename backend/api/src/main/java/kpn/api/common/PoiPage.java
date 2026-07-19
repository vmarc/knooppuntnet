package kpn.api.common;

import kpn.api.common.PoiAnalysis;

public record PoiPage(
  String elementType,
  Long elementId,
  String latitude,
  String longitude,
  PoiAnalysis analysis
) {
}

/*
package kpn.api.common

case class PoiPage(
  elementType: String,
  elementId: Long,
  latitude: String,
  longitude: String,
  analysis: PoiAnalysis
)

*/
