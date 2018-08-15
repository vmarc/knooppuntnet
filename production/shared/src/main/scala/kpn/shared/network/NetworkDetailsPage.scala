package kpn.shared.network

import kpn.shared.NetworkFacts
import kpn.shared.data.Tags

case class NetworkDetailsPage(
  networkSummary: NetworkSummary,
  active: Boolean,
  ignored: Boolean,
  attributes: NetworkAttributes,
  tags: Tags = Tags.empty,
  facts: NetworkFacts = NetworkFacts()
)
