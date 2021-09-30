package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.common.Ref
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Fact
import kpn.core.doc.NetworkInfoDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

class NetworkInfoUpdateAnalyzer(
  context: ChangeSetContext,
  before: NetworkInfoDoc,
  after: NetworkInfoDoc,
  networkId: Long
) {

  def analyze(): NetworkInfoChange = {

    val nodeDiffs = analyzeNodeDiffs(before, after)
    val routeDiffs = analyzeRouteDiffs(before, after)

    val extraNodeDiffs = analyzeExtraNodeDiffs()
    val extraWayDiffs = analyzeExtraWayDiffs()
    val extraRelationDiffs = analyzeExtraRelationDiffs()

    //  val networkDataBefore = NetworkData(
    //      MetaData(
    //        version: Long,
    //        timestamp: Timestamp,
    //        changeSetId: Long
    //      ),
    //      before.summary.name
    //  )

    //  val networkDataAfter = NetworkData(
    //      MetaData(
    //        version: Long,
    //        timestamp: Timestamp,
    //        changeSetId: Long
    //      ),
    //      after.summary.name
    //  )

    //  case class NetworkDataUpdate(
    //    before: NetworkData,
    //    after: NetworkData
    //  )
    val networkDataUpdate = None

    val orphanRouteDiffs = analyzeOrphanRouteDiffs()
    val orphanNodeDiffs = analyzeOrphanNodeDiffs()

    val happy = nodeDiffs.added.nonEmpty ||
      routeDiffs.added.nonEmpty ||
      extraNodeDiffs.removed.nonEmpty ||
      extraWayDiffs.removed.nonEmpty ||
      extraRelationDiffs.removed.nonEmpty

    val investigate = nodeDiffs.removed.nonEmpty ||
      routeDiffs.removed.nonEmpty ||
      extraNodeDiffs.added.nonEmpty ||
      extraWayDiffs.added.nonEmpty ||
      extraRelationDiffs.added.nonEmpty

    val impact = happy || investigate

    val key = context.buildChangeKey(networkId)
    NetworkInfoChange(
      key.toId,
      key,
      ChangeType.Update,
      after.country,
      after.scopedNetworkType.networkType,
      networkId,
      after.summary.name,
      orphanRouteDiffs,
      orphanNodeDiffs,
      networkDataUpdate,
      nodeDiffs,
      routeDiffs,
      extraNodeDiffs,
      extraWayDiffs,
      extraRelationDiffs,
      happy,
      investigate,
      impact
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

  private def analyzeExtraNodeDiffs(): IdDiffs = {
    analyzeIdDiffs(before.extraNodeIds, after.extraNodeIds)
  }

  private def analyzeExtraWayDiffs(): IdDiffs = {
    analyzeIdDiffs(before.extraWayIds, after.extraWayIds)
  }

  private def analyzeExtraRelationDiffs(): IdDiffs = {
    analyzeIdDiffs(before.extraRelationIds, after.extraRelationIds)
  }

  private def analyzeIdDiffs(idsBefore: Seq[Long], idsAfter: Seq[Long]): IdDiffs = {
    val idSetBefore = idsBefore.toSet
    val idSetAfter = idsAfter.toSet
    val removed = (idSetBefore -- idSetAfter).toSeq.sorted
    val added = (idSetAfter -- idSetBefore).toSeq.sorted
    IdDiffs(
      removed = removed,
      added = added
    )
  }

  private def analyzeNodeDiffs(before: NetworkInfoDoc, after: NetworkInfoDoc): RefDiffs = {
    val nodeIdsBefore = before.nodes.map(_.id).toSet
    val nodeIdsAfter = after.nodes.map(_.id).toSet
    val nodeIdsAdded = nodeIdsAfter -- nodeIdsBefore
    val nodeIdsRemoved = nodeIdsBefore -- nodeIdsAfter
    val nodeIdsCommon = nodeIdsBefore.intersect(nodeIdsAfter)
    val nodeRefsAdded = after.nodes.filter(node => nodeIdsAdded.contains(node.id)).map(_.toRef).sortBy(_.id)
    val nodeRefsRemoved = before.nodes.filter(node => nodeIdsRemoved.contains(node.id)).map(_.toRef).sortBy(_.id)
    val nodeRefsUpdated = nodeIdsCommon.toSeq.sorted.flatMap { nodeId =>
      before.nodes.find(node => node.id == nodeId).flatMap { nodeBefore =>
        after.nodes.find(node => node.id == nodeId).flatMap { nodeAfter =>
          if (!nodeBefore.isSameAs(nodeAfter)) {
            Some(Ref(nodeId, nodeAfter.name))
          }
          else {
            None
          }
        }
      }
    }
    RefDiffs(
      removed = nodeRefsRemoved,
      added = nodeRefsAdded,
      updated = nodeRefsUpdated
    )
  }

  private def analyzeRouteDiffs(before: NetworkInfoDoc, after: NetworkInfoDoc): RefDiffs = {
    val routeIdsBefore = before.routes.map(_.id).toSet
    val routeIdsAfter = after.routes.map(_.id).toSet
    val routeIdsAdded = routeIdsAfter -- routeIdsBefore
    val routeIdsRemoved = routeIdsBefore -- routeIdsAfter
    val routeIdsCommon = routeIdsBefore.intersect(routeIdsAfter)
    val routeRefsAdded = after.routes.filter(route => routeIdsAdded.contains(route.id)).map(_.toRef).sortBy(_.id)
    val routeRefsRemoved = before.routes.filter(route => routeIdsRemoved.contains(route.id)).map(_.toRef).sortBy(_.id)
    val routeRefsUpdated = routeIdsCommon.toSeq.sorted.flatMap { routeId =>
      before.routes.find(route => route.id == routeId).flatMap { routeBefore =>
        after.routes.find(route => route.id == routeId).flatMap { routeAfter =>
          if (!routeBefore.isSameAs(routeAfter)) {
            Some(Ref(routeId, routeAfter.name))
          }
          else {
            None
          }
        }
      }
    }
    RefDiffs(
      removed = routeRefsRemoved,
      added = routeRefsAdded,
      updated = routeRefsUpdated
    )
  }
}
