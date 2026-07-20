package kpn.api.common.network;

import kpn.api.common.common.Reference;
import kpn.api.common.network.NetworkNodeDetail;

import com.google.common.collect.ImmutableList;

public record NetworkNodeRow(
  NetworkNodeDetail detail,
  ImmutableList<Reference> routeReferences
) {}
