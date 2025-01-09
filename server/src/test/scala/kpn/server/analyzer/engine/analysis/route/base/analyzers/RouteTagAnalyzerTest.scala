package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteTagMissing
import kpn.api.common.NetworkScope
import kpn.api.common.NetworkScope.local
import kpn.api.common.NetworkScope.national
import kpn.api.common.NetworkScope.regional
import kpn.api.common.RouteType
import kpn.api.common.RouteType.canoe
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.api.common.RouteType.horseRiding
import kpn.api.common.RouteType.inlineSkating
import kpn.api.common.RouteType.motorboat
import kpn.api.common.SharedTestObjects
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.util.Redesign
import kpn.core.util.UnitTest

class RouteTagAnalyzerTest extends UnitTest with SharedTestObjects {

  test("route tag missing") {
    val context = analyze(
      Tags.from(
        "type" -> "route",
        "network:type" -> "node_network",
        "network" -> "rwn"
      )
    )
    assertEqual(context.facts, Seq(RouteTagMissing))
  }

  test("route tag valid") {
    testValid(local, hiking, "foot")
    testValid(regional, hiking, "hiking")
    testValid(national, hiking, "walking")
    testValid(local, cycling, "bicycle")
    testValid(regional, horseRiding, "horse")
    testValid(national, canoe, "canoe")
    testValid(local, motorboat, "motorboat")
    testValid(regional, inlineSkating, "inline_skates")
  }

  test("ignore additional values") {
    if (Redesign.enablePendingTests) {
      testValid(NetworkScope.regional, RouteType.cycling, "bicycle;mtb")
    }
  }

  private def testValid(networkScope: NetworkScope, routeType: RouteType, tagValue: String): Unit = {
    val scopedRouteType = ScopedRouteType(networkScope, routeType)
    val tags = Tags.from(
      "type" -> "route",
      "network:type" -> "node_network",
      "network" -> scopedRouteType.key,
      "route" -> tagValue
    )
    val context = analyze(tags)
    context.facts shouldBe empty
  }

  private def analyze(tags: Seq[Tag]): BaseRouteAnalysisContext = {
    val relation = newRelation(1L, tags = tags)
    val context = BaseRouteAnalysisContext(
      relation,
      None
    )
    BaseRouteTagAnalyzer.analyze(context)
  }
}
