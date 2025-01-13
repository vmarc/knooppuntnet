package kpn.server.analyzer.engine.changes.network

import kpn.api.common.diff.IdDiffs
import kpn.core.doc.NetworkDoc

class NetworkDiffAnalyzer(before: NetworkDoc, after: NetworkDoc) {

  def nodeDiffs: IdDiffs = {

    val nodesBefore = before.memberNodeIds.toSet
    val nodesAfter = after.memberNodeIds.toSet

    val nodesAdded = (nodesAfter -- nodesBefore).toSeq.sorted
    val nodesRemoved = (nodesBefore -- nodesAfter).toSeq.sorted

    val nodesUpdated = nodesBefore.intersect(nodesAfter).toSeq.filter { nodeId =>
      val nodeMemberBefore = before.nodeMembers.filter(_.ref == nodeId).head
      val nodeMemberAfter = after.nodeMembers.filter(_.ref == nodeId).head
      nodeMemberBefore.role != nodeMemberAfter.role
    }.sorted

    IdDiffs(
      added = nodesAdded,
      removed = nodesRemoved,
      updated = nodesUpdated
    )
  }

  def wayDiffs: IdDiffs = {

    val waysBefore = before.memberWayIds.toSet
    val waysAfter = after.memberWayIds.toSet

    val waysAdded = (waysAfter -- waysBefore).toSeq.sorted
    val waysRemoved = (waysBefore -- waysAfter).toSeq.sorted

    IdDiffs(
      added = waysAdded,
      removed = waysRemoved
    )
  }

  def relationDiffs: IdDiffs = {

    val relationsBefore = before.memberRelationIds.toSet
    val relationsAfter = after.memberRelationIds.toSet

    val relationsAdded = (relationsAfter -- relationsBefore).toSeq.sorted
    val relationsRemoved = (relationsBefore -- relationsAfter).toSeq.sorted

    val relationsUpdated = relationsBefore.intersect(relationsAfter).toSeq.filter { relationId =>
      val relationMemberBefore = before.relationMembers.filter(_.ref == relationId).head
      val relationMemberAfter = after.relationMembers.filter(_.ref == relationId).head
      relationMemberBefore.role != relationMemberAfter.role
    }.sorted

    IdDiffs(
      added = relationsAdded,
      removed = relationsRemoved,
      updated = relationsUpdated
    )
  }
}
