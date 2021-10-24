package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.ChangeType
import kpn.core.doc.NetworkInfoDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

class NetworkInfoCreateAnalyzer(context: ChangeSetContext, after: NetworkInfoDoc, networkId: Long) {

  def analyze(): NetworkInfoChange = {

    val nodeDiffs = RefDiffs(added = after.nodes.map(_.toRef))
    val routeDiffs = RefDiffs(added = after.routes.map(_.toRef))

    val extraNodeDiffs = IdDiffs(added = after.extraNodeIds)
    val extraWayDiffs = IdDiffs(added = after.extraWayIds)
    val extraRelationDiffs = IdDiffs(added = after.extraRelationIds)

    val investigate = extraNodeDiffs.added.nonEmpty ||
      extraWayDiffs.added.nonEmpty ||
      extraRelationDiffs.added.nonEmpty

    val key = context.buildChangeKey(networkId)
    NetworkInfoChange(
      key.toId,
      key,
      changeType = ChangeType.Create,
      country = after.country,
      networkType = after.scopedNetworkType.networkType,
      networkId = after._id,
      networkName = after.summary.name,
      networkDataUpdate = None,
      nodeDiffs,
      routeDiffs,
      extraNodeDiffs,
      extraWayDiffs,
      extraRelationDiffs,
      happy = true,
      investigate = investigate,
      impact = true
    )
  }
}
