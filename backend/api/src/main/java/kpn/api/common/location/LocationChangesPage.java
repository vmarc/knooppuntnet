package kpn.api.common.location;

import kpn.api.common.LocationChangeSetInfo;
import kpn.api.common.changes.filter.ChangesFilterOption;
import kpn.api.common.location.LocationSummary;

import com.google.common.collect.ImmutableList;

public record LocationChangesPage(
  LocationSummary summary,
  ImmutableList<LocationChangeSetInfo> changeSets,
  Long changesCount,
  ImmutableList<ChangesFilterOption> filterOptions
) {
}

/*
package kpn.api.common.location

import kpn.api.common.LocationChangeSetInfo
import kpn.api.common.changes.filter.ChangesFilterOption

case class LocationChangesPage(
  summary: LocationSummary,
  changeSets: Seq[LocationChangeSetInfo],
  changesCount: Long,
  filterOptions: Seq[ChangesFilterOption]
)

*/
