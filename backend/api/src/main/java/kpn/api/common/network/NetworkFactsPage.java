package kpn.api.common.network;

import kpn.api.common.NetworkFact;
import kpn.api.common.network.NetworkSummary;

import com.google.common.collect.ImmutableList;

public record NetworkFactsPage(
  NetworkSummary summary,
  ImmutableList<NetworkFact> facts
) {
}

/*
package kpn.api.common.network

import kpn.api.common.NetworkFact
import kpn.core.doc.Storable

case class NetworkFactsPage(
  summary: NetworkSummary,
  facts: Seq[NetworkFact]
) extends Storable

*/
