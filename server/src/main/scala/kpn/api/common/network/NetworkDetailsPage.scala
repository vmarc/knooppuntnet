package kpn.api.common.network

import kpn.api.common.Country
import kpn.api.custom.Tag

case class NetworkDetailsPage(
  summary: NetworkSummary,
  active: Boolean,
  country: Option[Country],
  detail: NetworkDetail,
  tags: Seq[Tag] = Seq.empty
)
