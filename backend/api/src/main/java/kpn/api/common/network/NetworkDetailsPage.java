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
) {
}

/*
package kpn.api.common.network

import kpn.api.common.Country
import kpn.api.custom.Tag

case class NetworkDetailsPage(
  summary: NetworkSummary,
  active: Boolean,
  country: Option[Country],
  detail: NetworkDetail,
  networkNodeIds: Seq[Long],
  connectionNodeIds: Seq[Long],
  networkRouteIds: Seq[Long],
  connectionRouteIds: Seq[Long],
  tags: Seq[Tag] = Seq.empty
)

*/
