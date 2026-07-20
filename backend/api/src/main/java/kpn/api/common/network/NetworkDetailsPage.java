package kpn.api.common.network;

import kpn.api.common.Country;
import kpn.api.common.network.NetworkDetail;
import kpn.api.common.network.NetworkSummary;
import kpn.api.custom.Tag;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record NetworkDetailsPage(
  NetworkSummary summary,
  Boolean active,
  Optional<Country> country,
  NetworkDetail detail,
  ImmutableList<Long> networkNodeIds,
  ImmutableList<Long> connectionNodeIds,
  ImmutableList<Long> networkRouteIds,
  ImmutableList<Long> connectionRouteIds,
  ImmutableList<Tag> tags
) {}
