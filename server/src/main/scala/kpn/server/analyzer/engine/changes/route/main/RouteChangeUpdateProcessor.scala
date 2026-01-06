package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.ChangeType
import kpn.api.common.ElementChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.route.RouteNodeChange
import kpn.core.doc.RouteDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.springframework.stereotype.Component

@Component
class RouteChangeUpdateProcessor() {

  // TODO verify that lostRouteTags are processed in base route processing (including route removed from analysisContext.watched.routes)
  /*
      val facts = if ((after.facts.contains(Fact.RouteTagMissing) && !before.facts.contains(Fact.RouteTagMissing)) ||
        (after.facts.contains(Fact.RouteTagInvalid) && !before.facts.contains(Fact.RouteTagInvalid))) {
        Seq(Fact.LostRouteTags)
      }
      else {
        Seq.empty
      }

   */
  def process(context: ChangeSetContext, before: RouteDoc, after: RouteDoc, routeId: Long): Option[RouteChangeContext] = {

    val baseRouteChangeOption = context.changes.baseRouteChanges.find(_.routeId == routeId)

    val impactedNodeIds: Seq[Long] = (before.base.nodes.nodeIds ++ after.base.nodes.nodeIds).distinct.sorted

    val beforeNetworkIds = before.networkReferences.map(_.id).toSet
    val afterNetworkIds = after.networkReferences.map(_.id).toSet

    val addedNetworkIds = (afterNetworkIds -- beforeNetworkIds).toSeq.sorted
    val removedNetworkIds = (beforeNetworkIds -- afterNetworkIds).toSeq.sorted
    val impactedNetworkIds = (beforeNetworkIds ++ afterNetworkIds).toSeq.sorted
    val addedToNetwork = after.networkReferences.filter(r => addedNetworkIds.contains(r.id)).map(_.toRef)
    val removedFromNetwork = before.networkReferences.filter(r => removedNetworkIds.contains(r.id)).map(_.toRef)

    val diffs = RouteDiff(None, None, None, Seq.empty, memberOrderChanged = false, None) // TODO this used to be produced by RouteDiffAnalyzer --> should move to base route processing?
    val nodeChanges = buildNodeChanges(before, after, diffs)

    val key = context.buildChangeKey(routeId)

    Some(
      RouteChangeContext(
        RouteChangeStateAnalyzer.analyzed(
          RouteChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Update,
            name = after.base.name,
            locationAnalysis = after.base.locationAnalysis,
            addedToNetwork = addedToNetwork,
            removedFromNetwork = removedFromNetwork,
            before = Some(RouteData.from(before)), // TODO can eliminate?
            after = Some(RouteData.from(after)), // TODO can eliminate?
            nodeChanges = nodeChanges,
            facts = Seq.empty // routeUpdate.facts  // TODO can eliminate?
          )
        ),
        impactedNodeIds = impactedNodeIds,
        impactedNetworkIds = impactedNetworkIds,
      )
    )
  }

  private def buildNodeChanges(before: RouteDoc, after: RouteDoc, diffs: RouteDiff): Seq[RouteNodeChange] = {
    val allNodes = before.base.nodes.nodes ++ after.base.nodes.nodes
    val allNodeIds = allNodes.map(_.nodeId).distinct

    val nodeIdsAdded = diffs.nodeDiffs.flatMap(_.added.map(_.id))
    val nodeIdsRemoved = diffs.nodeDiffs.flatMap(_.removed.map(_.id))

    val nodeChanges = allNodeIds.flatMap { nodeId =>
      val changeType = if (nodeIdsAdded.contains(nodeId)) {
        ElementChangeType.Added
      } else if (nodeIdsRemoved.contains(nodeId)) {
        ElementChangeType.Removed
      } else {
        before.base.nodes.nodes.find(_.nodeId == nodeId) match {
          case None => ElementChangeType.Unchanged
          case Some(nodeBefore) =>
            after.base.nodes.nodes.find(_.nodeId == nodeId) match {
              case None => ElementChangeType.Unchanged
              case Some(nodeAfter) =>
                if (nodeBefore.latitude == nodeAfter.latitude && nodeBefore.longitude == nodeAfter.longitude) {
                  ElementChangeType.Unchanged
                }
                else {
                  ElementChangeType.Changed
                }
            }
        }
      }
      val node = if (changeType == ElementChangeType.Removed) {
        before.base.nodes.nodes.find(_.nodeId == nodeId)
      }
      else {
        after.base.nodes.nodes.find(_.nodeId == nodeId)
      }

      node.map { node =>
        RouteNodeChange(
          nodeId,
          node.latitude,
          node.longitude,
          changeType
        )
      }
    }
    nodeChanges
  }
}
