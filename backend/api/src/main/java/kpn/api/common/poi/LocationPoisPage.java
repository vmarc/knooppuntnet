package kpn.api.common.poi;

import kpn.api.common.poi.LocationPoiInfo;

import com.google.common.collect.ImmutableList;

public record LocationPoisPage(
  Long poiCount,
  ImmutableList<LocationPoiInfo> pois
) {
}

/*
package kpn.api.common.poi

case class LocationPoisPage(
  poiCount: Long,
  pois: Seq[LocationPoiInfo]
)

*/
