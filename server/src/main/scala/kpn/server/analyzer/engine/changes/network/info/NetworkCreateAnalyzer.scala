package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

class NetworkCreateAnalyzer(context: ChangeSetContext, after: NetworkDoc, networkId: Long) {

  def analyze(): NetworkChange = {

    val nodeDiffs = RefDiffs(added = after.nodes.map(_.toRef))
    val routeDiffs = RefDiffs(added = after.routes.map(_.toRef))

    val extraNodeDiffs = IdDiffs(added = after.extraNodeIds)
    val extraWayDiffs = IdDiffs(added = after.extraWayIds)
    val extraRelationDiffs = IdDiffs(added = after.extraRelationIds)

    val nodes = IdDiffs(added = after.memberNodeIds)
    val ways = IdDiffs(added = after.memberWayIds)
    val relations = IdDiffs(added = after.memberRelationIds)

    val investigate = extraNodeDiffs.added.nonEmpty ||
      extraWayDiffs.added.nonEmpty ||
      extraRelationDiffs.added.nonEmpty

    val networkDataAfter = NetworkData(
      after.detail.toMeta,
      after.summary.name
    )

    val key = context.buildChangeKey(networkId)
    NetworkChange(
      key.toId,
      key,
      networkId = after._id,
      networkName = after.summary.name,
      changeType = ChangeType.Create,
      country = after.country,
      routeType = after.summary.routeType,
      networkDataUpdate = Some(
        NetworkDataUpdate(
          None,
          Some(networkDataAfter)
        )
      ),
      nodes,
      ways,
      relations,
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
