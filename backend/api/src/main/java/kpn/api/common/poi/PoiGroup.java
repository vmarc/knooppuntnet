package kpn.api.common.poi;

import com.google.common.collect.ImmutableList;

public record PoiGroup(
  String name,
  ImmutableList<PoiCount> poiCounts
) {}
