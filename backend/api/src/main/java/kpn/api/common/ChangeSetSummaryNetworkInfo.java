package kpn.api.common;

import kpn.api.common.ChangeSetSubsetElementRefs;
import kpn.api.common.NetworkChanges;

import com.google.common.collect.ImmutableList;

public record ChangeSetSummaryNetworkInfo(
  NetworkChanges networkChanges,
  ImmutableList<ChangeSetSubsetElementRefs> orphanRouteChanges,
  ImmutableList<ChangeSetSubsetElementRefs> orphanNodeChanges
) {
}
