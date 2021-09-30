package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Fact
import kpn.core.doc.NetworkInfoDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

class NetworkInfoCreateAnalyzer(context: ChangeSetContext, after: NetworkInfoDoc, networkId: Long) {

  def analyze(): NetworkInfoChange = {

    val orphanRouteDiffs = analyzeOrphanRouteDiffs()
    val orphanNodeDiffs = analyzeOrphanNodeDiffs()

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
      orphanRouteDiffs,
      orphanNodeDiffs,
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

  private def analyzeOrphanRouteDiffs(): RefChanges = {
    val oldOrphanRouteRefs = after.routes.map(_.id).flatMap { routeId =>
      context.changes.routeChanges.find(_.id == routeId) match {
        case None => None
        case Some(routeChange) =>
          if (routeChange.facts.contains(Fact.WasOrphan)) {
            Some(routeChange.toRef)
          }
          else {
            None
          }
      }
    }
    RefChanges(
      oldRefs = oldOrphanRouteRefs
    )
  }

  private def analyzeOrphanNodeDiffs(): RefChanges = {
    val oldOrphanNodeRefs = after.nodes.map(_.id).flatMap { nodeId =>
      context.changes.nodeChanges.find(_.id == nodeId) match {
        case None => None
        case Some(nodeChange) =>
          if (nodeChange.facts.contains(Fact.WasOrphan)) {
            Some(nodeChange.toRef)
          }
          else {
            None
          }
      }
    }
    RefChanges(
      oldRefs = oldOrphanNodeRefs
    )
  }
}
