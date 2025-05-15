package kpn.core.history

import kpn.api.common.data.Way
import kpn.api.common.diff.NodeUpdate
import kpn.api.common.diff.WayUpdate

class WayDiffAnalyzer(wayBefore: Way, wayAfter: Way) {

  def analysis: Option[WayUpdate] = {

    val nodeIdsBefore = wayBefore.nodes.map(_.id).toSet
    val nodeIdsAfter = wayAfter.nodes.map(_.id).toSet

    val removedNodeIds = (nodeIdsBefore -- nodeIdsAfter).toSeq.sorted
    val addedNodeIds = (nodeIdsAfter -- nodeIdsBefore).toSeq.sorted
    val commonNodeIds = nodeIdsBefore intersect nodeIdsAfter

    val nodeMapBefore = wayBefore.nodes.map(n => n.id -> n).toMap
    val nodeMapAfter = wayAfter.nodes.map(n => n.id -> n).toMap

    val updatedNodeIds = commonNodeIds.flatMap { nodeId =>
      val nodeBefore = nodeMapBefore(nodeId)
      val nodeAfter = nodeMapAfter(nodeId)
      if (nodeBefore == nodeAfter) {
        None
      }
      else {
        Some(nodeId)
      }
    }.toSeq.sorted

    val directionReversed = analyzeDirectionReversed

    val tagDiffs = new TagDiffAnalyzer(wayBefore, wayAfter).diffs

    Option.when(wayBefore.toRaw != wayAfter.toRaw || removedNodeIds.nonEmpty || addedNodeIds.nonEmpty || updatedNodeIds.nonEmpty || tagDiffs.isDefined || directionReversed) {
      WayUpdate(
        wayAfter.id,
        wayBefore.toMeta,
        wayAfter.toMeta,
        removedNodeIds.map(nodeId => nodeMapBefore(nodeId)),
        addedNodeIds.map(nodeId => nodeMapAfter(nodeId)),
        updatedNodeIds.map(nodeId => NodeUpdate(nodeMapBefore(nodeId), nodeMapAfter(nodeId), None, None)),
        directionReversed,
        tagDiffs
      )
    }
  }

  private def analyzeDirectionReversed: Boolean = wayBefore.nodeIds == wayAfter.nodeIds.reverse
}
