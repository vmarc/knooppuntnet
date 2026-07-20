package kpn.api.common.network;

import kpn.api.common.NetworkFact;
import kpn.api.common.network.NetworkSummary;
import kpn.core.doc.Storable;

import com.google.common.collect.ImmutableList;

public record NetworkFactsPage(
  NetworkSummary summary,
  ImmutableList<NetworkFact> facts
) implements Storable {}
