package kpn.api.common.network;

import kpn.api.common.changes.details.NetworkChangeInfo;
import kpn.api.common.changes.filter.ChangesFilterOption;
import kpn.api.common.network.NetworkSummary;

import com.google.common.collect.ImmutableList;

public record NetworkChangesPage(
  NetworkSummary network,
  ImmutableList<ChangesFilterOption> filterOptions,
  ImmutableList<NetworkChangeInfo> changes,
  Long totalCount
) {
}

/*
package kpn.api.common.network

import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.changes.filter.ChangesFilterOption

case class NetworkChangesPage(
  network: NetworkSummary,
  filterOptions: Seq[ChangesFilterOption],
  changes: Seq[NetworkChangeInfo],
  totalCount: Long
)

*/
