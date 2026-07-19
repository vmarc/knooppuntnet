package kpn.api.common.location;

import kpn.api.common.LocationInfo;
import kpn.api.common.location.LocationSummary;
import kpn.api.custom.Tag;

import com.google.common.collect.ImmutableList;

public record LocationDetailsPage(
  LocationSummary summary,
  Long relationId,
  Long distance,
  ImmutableList<LocationInfo> locationInfos,
  ImmutableList<Tag> tags
) {
}

/*
package kpn.api.common.location

import kpn.api.common.LocationInfo
import kpn.api.custom.Tag

case class LocationDetailsPage(
  summary: LocationSummary,
  relationId: Long,
  distance: Long,
  locationInfos: Seq[LocationInfo],
  tags: Seq[Tag]
)

*/
