package kpn.api.common;

import kpn.api.common.PoiAnalysis;

public record PoiPage(
  String elementType,
  Long elementId,
  String latitude,
  String longitude,
  PoiAnalysis analysis
) {}
