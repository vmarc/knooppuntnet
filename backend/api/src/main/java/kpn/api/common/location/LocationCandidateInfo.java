package kpn.api.common.location;

import kpn.api.common.LocationInfo;

import com.google.common.collect.ImmutableList;

public record LocationCandidateInfo(
  ImmutableList<LocationInfo> locationInfos,
  Long percentage
) {
}

/*
package kpn.api.common.location

import kpn.api.common.LocationInfo

case class LocationCandidateInfo(locationInfos: Seq[LocationInfo], percentage: Long)

*/
