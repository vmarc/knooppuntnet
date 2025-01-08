package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.RouteType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class RouteRouteTypeAnalyzerTest extends UnitTest {

  test("analyze route routeType") {
    assertEqual(routeTypes("hiking"), Seq(RouteType.hiking))
    assertEqual(routeTypes("walking"), Seq(RouteType.hiking))
    assertEqual(routeTypes("foot"), Seq(RouteType.hiking))
    assertEqual(routeTypes("bicycle"), Seq(RouteType.cycling))
    assertEqual(routeTypes("horse"), Seq(RouteType.horseRiding))
    assertEqual(routeTypes("canoe"), Seq(RouteType.canoe))
    assertEqual(routeTypes("motorboat"), Seq(RouteType.motorboat))
    assertEqual(routeTypes("inline_skates"), Seq(RouteType.inlineSkating))
    assertEqual(routeTypes("hiking;bicycle"), Seq(RouteType.hiking, RouteType.cycling))
    assertEqual(routeTypes("hiking; horse "), Seq(RouteType.hiking, RouteType.horseRiding))
    assertEqual(routeTypes("hiking;bla"), Seq(RouteType.hiking))
    assertEqual(routeTypes("bla"), Seq.empty)
  }

  private def routeTypes(routeTagValue: String): Seq[RouteType] = {
    val tags = Tags.from("route" -> routeTagValue)
    new RouterouteTypeAnalyzer(tags).analyze()
  }
}
