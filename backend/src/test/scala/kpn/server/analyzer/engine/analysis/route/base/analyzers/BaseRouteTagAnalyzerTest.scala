package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteTagMissing
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newRelation
import kpn.core.util.UnitTest

class BaseRouteTagAnalyzerTest extends UnitTest {

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
    //    testValid(RouteScope.local, RouteType.hiking, "foot")
    //    testValid(RouteScope.regional, RouteType.hiking, "hiking")
    //    testValid(RouteScope.national, RouteType.hiking, "walking")
    //    testValid(RouteScope.local, RouteType.cycling, "bicycle")
    //    testValid(RouteScope.regional, RouteType.horseRiding, "horse")
    //    testValid(RouteScope.national, RouteType.canoe, "canoe")
    //    testValid(RouteScope.local, RouteType.motorboat, "motorboat")
    //    testValid(RouteScope.regional, RouteType.inlineSkating, "inline_skates")
    testValid(RouteScope.regional, RouteType.mtb, "mtb")
  }

  test("ignore additional values") {
    testValid(RouteScope.regional, RouteType.cycling, "bicycle;bla")
  }

  test("unknown 'network' tag value") {
    val context = analyze(
      Tags.from(
        "type" -> "route",
        "network:type" -> "node_network",
        "network" -> "Alabak", // routeId=301671
        "route" -> "mtb"
      )
    )
    context.scopedRouteType should equal(None)
  }

  private def testValid(routeScope: RouteScope, routeType: RouteType, tagValue: String): Unit = {
    val scopedRouteType = ScopedRouteType(routeType, routeScope)
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
