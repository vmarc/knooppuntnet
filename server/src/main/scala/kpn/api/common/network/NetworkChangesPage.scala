package kpn.api.common.network

import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.changes.filter.ChangesFilter

case class NetworkChangesPage(
  network: NetworkSummary,
  filter: ChangesFilter,
  changes: Seq[NetworkChangeInfo],
  totalCount: Int) {
}
