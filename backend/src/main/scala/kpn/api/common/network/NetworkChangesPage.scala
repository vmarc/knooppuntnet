package kpn.api.common.network

import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.changes.filter.ChangesFilterOption

case class NetworkChangesPage(
  network: NetworkSummary,
  filterOptions: Seq[ChangesFilterOption],
  changes: Seq[NetworkChangeInfo],
  totalCount: Long
)
