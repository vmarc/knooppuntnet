package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newRelation
import kpn.core.util.UnitTest

class BaseRouteTypeAnalyzerTest extends UnitTest {

  test("single route type") {
    assertRouteTypes("hiking", RouteType.hiking)
    assertRouteTypes("walking", RouteType.hiking)
    assertRouteTypes("foot", RouteType.hiking)
    assertRouteTypes("bicycle", RouteType.cycling)
    assertRouteTypes("horse", RouteType.horseRiding)
    assertRouteTypes("canoe", RouteType.canoe)
    assertRouteTypes("motorboat", RouteType.motorboat)
    assertRouteTypes("inline_skates", RouteType.inlineSkating)
    assertRouteTypes("mtb", RouteType.mtb)
  }

  test("multiple route types - hiking and cycling") {
    assertRouteTypes("hiking;bicycle", RouteType.cycling, RouteType.hiking)
  }

  test("duplicated route types") {
    assertRouteTypes("hiking;hiking", RouteType.hiking)
  }

  test("malformed input") {
    assertRouteTypes("hiking;;bicycle", RouteType.cycling, RouteType.hiking)
  }

  test("filter out unsupported route types") {
    assertRouteTypes("hiking;jos;bla", RouteType.hiking)
  }

  test("abort analysis when route tag is missing") {
    assertAbortAnalysis(Seq.empty)
  }

  test("abort analysis when empty route tag value") {
    assertAbortAnalysis(Tags.from("route" -> ""))
  }

  test("abort analysis when whitespace-only route tag") {
    assertAbortAnalysis(Tags.from("route" -> "   "))
  }

  private def assertRouteTypes(routeTagValue: String, expectedRouteTypes: RouteType*): Unit = {
    val tags = if (routeTagValue.nonEmpty) Tags.from("route" -> routeTagValue) else Seq.empty
    val context = analyze(tags)
    context.facts should equal(Seq.empty)
    context.abort should equal(false)
    context.routeTypes should equal(expectedRouteTypes)
  }

  private def assertAbortAnalysis(tags: Seq[Tag]): Unit = {
    val context = analyze(tags)
    context.facts should equal(Seq(Fact.RouteUnsupportedRouteType))
    context.abort should equal(true)
  }

  private def analyze(tags: Seq[Tag]): BaseRouteAnalysisContext = {
    val relation = newRelation(tags = tags)
    val initialContext = BaseRouteAnalysisContext(relation, None)
    BaseRouteTypeAnalyzer.analyze(initialContext)
  }
}
