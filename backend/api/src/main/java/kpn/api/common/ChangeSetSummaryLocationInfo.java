package kpn.api.common;

import kpn.api.common.LocationChanges;

import com.google.common.collect.ImmutableList;

public record ChangeSetSummaryLocationInfo(
  ImmutableList<LocationChanges> changes
) {
}
