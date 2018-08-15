package kpn.shared.network

import kpn.shared.changes.details.NetworkChangeInfo
import kpn.shared.changes.filter.ChangesFilter

case class NetworkChangesPage(
  network: NetworkInfo,
  filter: ChangesFilter,
  changes: Seq[NetworkChangeInfo],
  totalCount: Int
) {
  def networkId: Long = network.id
}
