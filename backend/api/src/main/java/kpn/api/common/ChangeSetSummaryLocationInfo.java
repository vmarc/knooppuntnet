package kpn.api.common;

import kpn.api.common.LocationChanges;

import com.google.common.collect.ImmutableList;

public record ChangeSetSummaryLocationInfo(
  ImmutableList<LocationChanges> changes
) {
}

/*
package kpn.api.common

case class ChangeSetSummaryLocationInfo(
  changes: Seq[LocationChanges]
)

*/
