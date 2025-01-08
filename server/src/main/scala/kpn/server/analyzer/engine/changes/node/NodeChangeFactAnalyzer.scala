package kpn.server.analyzer.engine.changes.node

import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.common.data.Node
import kpn.core.analysis.TagInterpreter
import kpn.server.analyzer.engine.context.AnalysisContext

// TODO redesign - cleanup if not used anymore
class NodeChangeFactAnalyzer(analysisContext: AnalysisContext) {

  def facts(before: Node, after: Node): Seq[Fact] = {
    Seq(
      test(Fact.LostHikingNodeTag, hasLostNodeTag(RouteType.hiking, before, after)),
      test(Fact.LostBicycleNodeTag, hasLostNodeTag(RouteType.cycling, before, after)),
      test(Fact.LostHorseNodeTag, hasLostNodeTag(RouteType.horseRiding, before, after)),
      test(Fact.LostMotorboatNodeTag, hasLostNodeTag(RouteType.motorboat, before, after)),
      test(Fact.LostCanoeNodeTag, hasLostNodeTag(RouteType.canoe, before, after)),
      test(Fact.LostInlineSkateNodeTag, hasLostNodeTag(RouteType.inlineSkating, before, after))
    ).flatten
  }

  private def hasLostNodeTag(routeType: RouteType, before: Node, after: Node): Boolean = {
    val nodeTagBefore: Boolean = TagInterpreter.isValidNetworkNode(routeType, before)
    val nodeTagAfter: Boolean = TagInterpreter.isValidNetworkNode(routeType, after)
    nodeTagBefore && !nodeTagAfter
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
