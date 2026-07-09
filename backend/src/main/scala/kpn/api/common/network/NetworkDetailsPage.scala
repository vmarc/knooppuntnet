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
