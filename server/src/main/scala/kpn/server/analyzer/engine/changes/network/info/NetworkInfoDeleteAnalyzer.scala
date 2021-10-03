package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.ChangeType
import kpn.api.custom.Fact
import kpn.core.doc.NetworkInfoDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

class NetworkInfoDeleteAnalyzer(context: ChangeSetContext, before: NetworkInfoDoc, networkId: Long) {

  def analyze(): NetworkInfoChange = {

    val orphanRouteDiffs = analyzeOrphanRouteDiffs()
    val orphanNodeDiffs = analyzeOrphanNodeDiffs()

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
      orphanRouteDiffs,
      orphanNodeDiffs,
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

  private def analyzeOrphanRouteDiffs(): RefChanges = {
    val newOrphanRouteRefs = before.routes.map(_.id).flatMap { routeId =>
      context.changes.routeChanges.find(_.id == routeId) match {
        case None => None
        case Some(routeChange) =>
          if (routeChange.facts.contains(Fact.BecomeOrphan)) {
            Some(routeChange.toRef)
          }
          else {
            None
          }
      }
    }
    RefChanges(
      newRefs = newOrphanRouteRefs
    )
  }

  private def analyzeOrphanNodeDiffs(): RefChanges = {
    val newOrphanNodeRefs = before.nodes.map(_.id).flatMap { nodeId =>
      context.changes.nodeChanges.find(_.id == nodeId) match {
        case None => None
        case Some(nodeChange) =>
          if (nodeChange.facts.contains(Fact.BecomeOrphan)) {
            Some(nodeChange.toRef)
          }
          else {
            None
          }
      }
    }
    RefChanges(
      newRefs = newOrphanNodeRefs
    )
  }
}
