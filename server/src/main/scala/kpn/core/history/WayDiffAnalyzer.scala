package kpn.core.history

import kpn.api.common.data.Way
import kpn.api.common.diff.WayUpdate

class WayDiffAnalyzer(wayBefore: Way, wayAfter: Way) {

  def analysis: Option[WayUpdate] = {

    val nodeIdsBefore = wayBefore.nodeIds.toSet
    val nodeIdsAfter = wayAfter.nodeIds.toSet

    val removedNodeIds = (nodeIdsBefore -- nodeIdsAfter).toSeq.sorted
    val addedNodeIds = (nodeIdsAfter -- nodeIdsBefore).toSeq.sorted
    val commonNodeIds = nodeIdsBefore intersect nodeIdsAfter

    val directionReversed = analyzeDirectionReversed

    val tagDiffs = new TagDiffAnalyzer(wayBefore, wayAfter).diffs

    Option.when(wayBefore != wayAfter || removedNodeIds.nonEmpty || addedNodeIds.nonEmpty || tagDiffs.isDefined || directionReversed) {
      WayUpdate(
        wayAfter.id,
        wayBefore.toMeta,
        wayAfter.toMeta,
        removedNodeIds,
        addedNodeIds,
        directionReversed,
        tagDiffs
      )
    }
  }

  private def analyzeDirectionReversed: Boolean = wayBefore.nodeIds == wayAfter.nodeIds.reverse
}
