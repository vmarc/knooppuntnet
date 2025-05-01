package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

class NetworkDeleteAnalyzer(context: ChangeSetContext, before: NetworkDoc, networkId: Long) {

  def analyze(): NetworkChange = {

    val nodeDiffs = RefDiffs(removed = before.nodes.map(_.toRef))
    val routeDiffs = RefDiffs(removed = before.routes.map(_.toRef))

    val extraNodeDiffs = IdDiffs(removed = before.extraNodeIds)
    val extraWayDiffs = IdDiffs(removed = before.extraWayIds)
    val extraRelationDiffs = IdDiffs(removed = before.extraRelationIds)

    val nodes: IdDiffs = IdDiffs(removed = before.memberNodeIds)
    val ways: IdDiffs = IdDiffs(removed = before.memberWayIds)
    val relations: IdDiffs = IdDiffs(removed = before.memberRelationIds)

    val key = context.buildChangeKey(networkId)
    NetworkChange(
      key.toId,
      key,
      networkId = networkId,
      networkName = before.summary.name,
      changeType = ChangeType.Delete,
      country = before.country,
      routeType = before.summary.routeType,
      networkDataUpdate = None,
      nodes,
      ways,
      relations,
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
