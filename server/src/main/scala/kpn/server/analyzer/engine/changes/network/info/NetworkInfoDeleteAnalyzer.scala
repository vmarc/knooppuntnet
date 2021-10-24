package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.ChangeType
import kpn.core.doc.NetworkInfoDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

class NetworkInfoDeleteAnalyzer(context: ChangeSetContext, before: NetworkInfoDoc, networkId: Long) {

  def analyze(): NetworkInfoChange = {

    val nodeDiffs = RefDiffs(removed = before.nodes.map(_.toRef))
    val routeDiffs = RefDiffs(removed = before.routes.map(_.toRef))

    val extraNodeDiffs = IdDiffs(removed = before.extraNodeIds)
    val extraWayDiffs = IdDiffs(removed = before.extraWayIds)
    val extraRelationDiffs = IdDiffs(removed = before.extraRelationIds)

    val key = context.buildChangeKey(networkId)
    NetworkInfoChange(
      key.toId,
      key,
      changeType = ChangeType.Delete,
      country = before.country,
      networkType = before.scopedNetworkType.networkType,
      networkId = networkId,
      networkName = before.summary.name,
      networkDataUpdate = None,
      nodeDiffs,
      routeDiffs,
      extraNodeDiffs,
      extraWayDiffs,
      extraRelationDiffs,
      happy = false,
      investigate = true,
      impact = true
    )
  }
}
