package kpn.core.engine.changes.node

import kpn.core.engine.changes.data.AnalysisData
import kpn.shared.Fact
import kpn.shared.data.raw.RawNode

class NodeChangeFactAnalyzer(
  analysisData: AnalysisData
) {

  def facts(before: RawNode, after: RawNode): Seq[Fact] = {
    Seq(
      test(Fact.LostHikingNodeTag, hasLostHikingNodeTag(before, after)),
      test(Fact.LostBicycleNodeTag, hasLostBicycleNodeTag(before, after)),
      test(Fact.WasOrphan, wasOrphan(before, after)),
      test(Fact.WasIgnored, wasIgnored(before, after))
    ).flatten
  }

  private def hasLostHikingNodeTag(before: RawNode, after: RawNode) = {
    before.tags.hasHikingNodeTag && !after.tags.hasHikingNodeTag
  }

  private def hasLostBicycleNodeTag(before: RawNode, after: RawNode) = {
    before.tags.hasBicyleNodeTag && !after.tags.hasBicyleNodeTag
  }

  private def wasOrphan(before: RawNode, after: RawNode) = {
    !after.tags.hasNodeTag && analysisData.orphanNodes.watched.contains(before.id)
  }

  private def wasIgnored(before: RawNode, after: RawNode) = {
    !after.tags.hasNodeTag && analysisData.orphanNodes.ignored.contains(before.id)
  }

  private def test(fact: Fact, exists: Boolean): Seq[Fact] = {
    if (exists) {
      Seq(fact)
    }
    else {
      Seq()
    }
  }
}
