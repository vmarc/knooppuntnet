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

    val tagDiffs = new TagDiffAnalyzer(wayBefore.raw, wayAfter.raw).diffs

    if (wayBefore.raw != wayAfter.raw || removedNodeIds.nonEmpty || addedNodeIds.nonEmpty || updatedNodeIds.nonEmpty || tagDiffs.isDefined || directionReversed) {
      Some(
        WayUpdate(
          wayAfter.id,
          wayBefore.toMeta,
          wayAfter.toMeta,
          removedNodeIds.map(nodeId => nodeMapBefore(nodeId)).map(_.raw),
          addedNodeIds.map(nodeId => nodeMapAfter(nodeId)).map(_.raw),
          updatedNodeIds.map(nodeId => NodeUpdate(nodeMapBefore(nodeId).raw, nodeMapAfter(nodeId).raw, None, None)),
          directionReversed,
          tagDiffs
        )
      )
    }
    else {
      None
    }
  }

  private def analyzeDirectionReversed: Boolean = wayBefore.raw.nodeIds == wayAfter.raw.nodeIds.reverse

}
