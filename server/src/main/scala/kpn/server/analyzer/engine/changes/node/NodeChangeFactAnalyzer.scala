package kpn.server.analyzer.engine.changes.node

import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.changes.data.AnalysisData
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.data.raw.RawNode

class NodeChangeFactAnalyzer(analysisData: AnalysisData) {

  def facts(before: RawNode, after: RawNode): Seq[Fact] = {
    Seq(
      test(Fact.LostHikingNodeTag, hasLostNodeTag(NetworkType.hiking, before, after)),
      test(Fact.LostBicycleNodeTag, hasLostNodeTag(NetworkType.bicycle, before, after)),
      test(Fact.LostHorseNodeTag, hasLostNodeTag(NetworkType.horseRiding, before, after)),
      test(Fact.LostMotorboatNodeTag, hasLostNodeTag(NetworkType.motorboat, before, after)),
      test(Fact.LostCanoeNodeTag, hasLostNodeTag(NetworkType.canoe, before, after)),
      test(Fact.LostInlineSkateNodeTag, hasLostNodeTag(NetworkType.inlineSkates, before, after)),
      test(Fact.WasOrphan, wasOrphan(before, after))
    ).flatten
  }

  private def hasLostNodeTag(networkType: NetworkType, before: RawNode, after: RawNode) = {
    hasNodeTag(networkType, before) && !hasNodeTag(networkType, after)
  }

  private def hasNodeTag(networkType: NetworkType, node: RawNode): Boolean = {
    networkType.scopedNetworkTypes.exists(s => node.tags.has(s.nodeTagKey))
  }

  private def wasOrphan(before: RawNode, after: RawNode) = {
    !NodeAnalyzer.hasNodeTag(after.tags) && analysisData.orphanNodes.watched.contains(before.id)
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
