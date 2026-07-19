package kpn.api.common.poi;

import kpn.api.common.poi.PoiCount;

import com.google.common.collect.ImmutableList;

public record PoiGroup(
  String name,
  ImmutableList<PoiCount> poiCounts
) {
}

/*
package kpn.api.common.poi

case class PoiGroup(name: String, poiCounts: Seq[PoiCount])

*/
