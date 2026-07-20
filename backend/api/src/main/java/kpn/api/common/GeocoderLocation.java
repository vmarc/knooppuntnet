package kpn.api.common;

import kpn.api.common.Bounds;

public record GeocoderLocation(
  String name,
  String latitude,
  String longitude,
  Bounds bounds
) {}
