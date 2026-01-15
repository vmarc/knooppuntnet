package kpn.server.api.analysis.pages.network

import kpn.api.common.Country
import kpn.api.common.network.NetworkDetail
import kpn.api.common.network.NetworkSummary
import kpn.api.custom.Tag
import kpn.core.doc.Storable

case class NetworkDetailsPageData(
  summary: NetworkSummary,
  active: Boolean,
  country: Option[Country],
  detail: NetworkDetail,
  tags: Seq[Tag]
) extends Storable
