package kpn.api.common.network;

import kpn.api.common.Bounds;
import kpn.api.common.network.NetworkMapNode;
import kpn.api.common.network.NetworkSummary;

import com.google.common.collect.ImmutableList;

public record NetworkMapPage(
  NetworkSummary summary,
  ImmutableList<NetworkMapNode> nodes,
  ImmutableList<Long> networkNodeIds,
  ImmutableList<Long> connectionNodeIds,
  ImmutableList<Long> networkRouteIds,
  ImmutableList<Long> connectionRouteIds,
  Bounds bounds
) {
}
