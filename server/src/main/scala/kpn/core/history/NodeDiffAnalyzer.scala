package kpn.core.history

import kpn.api.common.data.raw.RawNode
import kpn.api.common.diff.NodeUpdate
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.node.NodeMoved

class NodeDiffAnalyzer(before: RawNode, after: RawNode) {

  def analysis: Option[NodeUpdate] = {
    val tagDiffs = analyzeTagDiffs
    val nodeMoved = analyzeNodeMoved
    if (before != after || tagDiffs.isDefined || nodeMoved.isDefined) {
      Some(NodeUpdate(before, after, tagDiffs, nodeMoved))
    }
    else {
      None
    }
  }

  private def analyzeTagDiffs: Option[TagDiffs] = {
    new NodeTagDiffAnalyzer(before, after).diffs
  }

  private def analyzeNodeMoved: Option[NodeMoved] = {
    new NodeMovedAnalyzer(before, after).analysis
  }
}
