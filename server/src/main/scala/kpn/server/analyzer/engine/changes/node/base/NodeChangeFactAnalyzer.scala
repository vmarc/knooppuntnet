package kpn.server.analyzer.engine.changes.node.base

import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.common.data.Tagable
import kpn.core.analysis.TagInterpreter

object NodeChangeFactAnalyzer {

  def facts(before: Tagable, after: Tagable): Seq[Fact] = {
    Seq(
      test(Fact.LostHikingNodeTag, hasLostNodeTag(RouteType.hiking, before, after)),
      test(Fact.LostBicycleNodeTag, hasLostNodeTag(RouteType.cycling, before, after)),
      test(Fact.LostHorseNodeTag, hasLostNodeTag(RouteType.horseRiding, before, after)),
      test(Fact.LostMotorboatNodeTag, hasLostNodeTag(RouteType.motorboat, before, after)),
      test(Fact.LostCanoeNodeTag, hasLostNodeTag(RouteType.canoe, before, after)),
      test(Fact.LostInlineSkateNodeTag, hasLostNodeTag(RouteType.inlineSkating, before, after))
    ).flatten
  }

  private def hasLostNodeTag(routeType: RouteType, before: Tagable, after: Tagable): Boolean = {
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
