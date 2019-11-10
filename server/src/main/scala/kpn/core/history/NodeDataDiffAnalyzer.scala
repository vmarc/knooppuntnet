package kpn.core.history

import kpn.api.common.diff.NodeData
import kpn.api.common.diff.NodeDataUpdate
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.node.NodeMoved

class NodeDataDiffAnalyzer(before: NodeData, after: NodeData) {

  def analysis: Option[NodeDataUpdate] = {
    val tagDiffs = analyzeTagDiffs
    val nodeMoved = analyzeNodeMoved
    if (before != after || tagDiffs.isDefined || nodeMoved.isDefined) {
      Some(NodeDataUpdate(before, after, tagDiffs, nodeMoved))
    }
    else {
      None
    }
  }

  private def analyzeTagDiffs: Option[TagDiffs] = {
    new NodeTagDiffAnalyzer(before.node, after.node).diffs
  }

  private def analyzeNodeMoved: Option[NodeMoved] = {
    new NodeMovedAnalyzer(before.node, after.node).analysis
  }
}
