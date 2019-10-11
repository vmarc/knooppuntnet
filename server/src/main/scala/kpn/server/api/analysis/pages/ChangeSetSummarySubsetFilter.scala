package kpn.server.api.analysis.pages

import kpn.shared.ChangeSetSummary
import kpn.shared.NetworkChanges
import kpn.shared.Subset

object ChangeSetSummarySubsetFilter {

  def filter(changeSetSummary: ChangeSetSummary, subset: Subset): ChangeSetSummary = {

    val filteredNetworkChanges = NetworkChanges(
      creates = changeSetSummary.networkChanges.creates.filter(_.subsets.contains(subset)),
      updates = changeSetSummary.networkChanges.updates.filter(_.subsets.contains(subset)),
      deletes = changeSetSummary.networkChanges.deletes.filter(_.subsets.contains(subset))
    )
    val filteredOrphanRouteChanges = changeSetSummary.orphanRouteChanges.filter(_.subset == subset)
    val filteredOrphanNodeChanges = changeSetSummary.orphanNodeChanges.filter(_.subset == subset)

    ChangeSetSummary(
      changeSetSummary.key,
      changeSetSummary.timestampFrom,
      changeSetSummary.timestampUntil,
      filteredNetworkChanges,
      filteredOrphanRouteChanges,
      filteredOrphanNodeChanges
    )
  }
}
