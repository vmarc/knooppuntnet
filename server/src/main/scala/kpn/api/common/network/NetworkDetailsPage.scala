package kpn.api.common.network

import kpn.api.common.NetworkFacts
import kpn.api.custom.Tags

case class NetworkDetailsPage(
  summary: NetworkSummary,
  active: Boolean,
  attributes: NetworkAttributes,
  tags: Tags = Tags.empty,
  facts: NetworkFacts = NetworkFacts()
)
