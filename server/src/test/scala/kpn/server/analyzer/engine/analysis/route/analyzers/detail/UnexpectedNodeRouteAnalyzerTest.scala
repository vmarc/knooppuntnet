package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.Fact.RouteUnexpectedNode
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class UnexpectedNodeRouteAnalyzerTest extends UnitTest {

  test("no unexpected nodes") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
    }

    val context = analyze(d)

    context.facts shouldBe empty
    assertEqual(context._unexpectedNodeIds, Some(Seq.empty))
  }

  test("random node is unexpected") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
      rawNode(newRawNode(1003, tags = Tags.from("building" -> "church")))
      memberNode(1003)
    }

    val context = analyze(d)

    assertEqual(context.facts, Seq(RouteUnexpectedNode))
    assertEqual(context._unexpectedNodeIds, Some(Seq(1003)))
  }

  test("network node with different scope is unexpected") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
      rawNode(newRawNode(1003, tags = Tags.from("rcn_ref" -> "01")))
      memberNode(1003)
    }

    val context = analyze(d)

    assertEqual(context.facts, Seq(RouteUnexpectedNode))
    assertEqual(context._unexpectedNodeIds, Some(Seq(1003)))
  }

  test("maps and guideposts/route markers are expected") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
      rawNode(newRawNode(1003, tags = Tags.from("tourism" -> "information", "information" -> "map")))
      rawNode(newRawNode(1004, tags = Tags.from("tourism" -> "information", "information" -> "guidepost")))
      rawNode(newRawNode(1005, tags = Tags.from("tourism" -> "information", "information" -> "route_marker")))
      memberNode(1003)
      memberNode(1004)
      memberNode(1005)
    }

    val context = analyze(d)

    assertEqual(context.facts, Seq.empty)
    assertEqual(context._unexpectedNodeIds, Some(Seq.empty))
  }

  test("no unexpected nodes in non node network routes") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
    }

    val context = analyze(d, nodeNetwork = false)

    context.facts shouldBe empty
    assertEqual(context._unexpectedNodeIds, Some(Seq.empty))
  }

  private def analyze(routeTestData: RouteTestData, nodeNetwork: Boolean = true): RouteDetailAnalysisContext = {

    val data = routeTestData.data
    val relation = data.relations(1L)

    val context = RouteDetailAnalysisContext(
      relation,
      None,
      nodeNetwork = nodeNetwork,
      scopedNetworkTypeOption = Some(routeTestData.scopedNetworkType)
    )

    UnexpectedNodeRouteAnalyzer.analyze(context)
  }
}
