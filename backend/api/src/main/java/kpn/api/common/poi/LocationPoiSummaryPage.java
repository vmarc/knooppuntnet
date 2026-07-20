package kpn.api.common.poi;

import kpn.api.common.poi.PoiGroup;

import com.google.common.collect.ImmutableList;

public record LocationPoiSummaryPage(
  ImmutableList<PoiGroup> groups
) {}
