package kpn.api.common.network

import kpn.api.common.NetworkFacts
import kpn.api.custom.Tag

case class NetworkDetailsPage(
  summary: NetworkSummary,
  active: Boolean,
  attributes: NetworkAttributes,
  tags: Seq[Tag] = Seq.empty,
  facts: NetworkFacts = NetworkFacts()
)
