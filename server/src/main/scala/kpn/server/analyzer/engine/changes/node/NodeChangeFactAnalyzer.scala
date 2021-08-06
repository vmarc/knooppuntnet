package kpn.server.analyzer.engine.changes.node

import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.core.analysis.TagInterpreter
import kpn.server.analyzer.engine.context.AnalysisContext

class NodeChangeFactAnalyzer(analysisContext: AnalysisContext) {

  def facts(before: RawNode, after: RawNode): Seq[Fact] = {
    Seq(
      test(Fact.LostHikingNodeTag, hasLostNodeTag(NetworkType.hiking, before, after)),
      test(Fact.LostBicycleNodeTag, hasLostNodeTag(NetworkType.cycling, before, after)),
      test(Fact.LostHorseNodeTag, hasLostNodeTag(NetworkType.horseRiding, before, after)),
      test(Fact.LostMotorboatNodeTag, hasLostNodeTag(NetworkType.motorboat, before, after)),
      test(Fact.LostCanoeNodeTag, hasLostNodeTag(NetworkType.canoe, before, after)),
      test(Fact.LostInlineSkateNodeTag, hasLostNodeTag(NetworkType.inlineSkating, before, after)),
      test(Fact.WasOrphan, wasOrphan(before, after))
    ).flatten
  }

  private def hasLostNodeTag(networkType: NetworkType, before: RawNode, after: RawNode): Boolean = {
    val nodeTagBefore: Boolean = TagInterpreter.isValidNetworkNode(networkType, before)
    val nodeTagAfter: Boolean = TagInterpreter.isValidNetworkNode(networkType, after)
    nodeTagBefore && !nodeTagAfter
  }

  private def wasOrphan(before: RawNode, after: RawNode) = {
    !TagInterpreter.isValidNetworkNode(after) &&
      analysisContext.data.orphanNodes.watched.contains(before.id)
  }

  private def test(fact: Fact, exists: Boolean): Seq[Fact] = {
    if (exists) {
      Seq(fact)
    }
    else {
      Seq.empty
    }
  }
}
