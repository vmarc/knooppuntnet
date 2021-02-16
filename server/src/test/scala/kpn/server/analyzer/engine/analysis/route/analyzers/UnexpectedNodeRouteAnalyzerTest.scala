package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute

class UnexpectedNodeRouteAnalyzerTest extends UnitTest {

  test("no unexpected nodes") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
    }

    val context = analyze(d)

    context.facts should equal(Seq())
    context.unexpectedNodeIds should equal(Some(Seq()))
  }

  test("random node is unexpected") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
      rawNode(newRawNode(1003, tags = Tags.from("building" -> "church")))
      memberNode(1003)
    }

    val context = analyze(d)

    context.facts should equal(Seq(Fact.RouteUnexpectedNode))
    context.unexpectedNodeIds should equal(Some(Seq(1003)))
  }

  test("network node with different scope is unexpected") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
      rawNode(newRawNode(1003, tags = Tags.from("rcn_ref" -> "01")))
      memberNode(1003)
    }

    val context = analyze(d)

    context.facts should equal(Seq(Fact.RouteUnexpectedNode))
    context.unexpectedNodeIds should equal(Some(Seq(1003)))
  }

  test("maps and guideposts are expected") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
      rawNode(newRawNode(1003, tags = Tags.from("tourism" -> "information", "information" -> "map")))
      rawNode(newRawNode(1004, tags = Tags.from("tourism" -> "information", "information" -> "guidepost")))
      memberNode(1003)
      memberNode(1004)
    }

    val context = analyze(d)

    context.facts should equal(Seq())
    context.unexpectedNodeIds should equal(Some(Seq()))
  }


  private def analyze(routeTestData: RouteTestData): RouteAnalysisContext = {

    val data = routeTestData.data

    val loadedRoute = LoadedRoute(
      country = None,
      scopedNetworkType = routeTestData.scopedNetworkType,
      "",
      data,
      data.relations(1L)
    )

    val context = RouteAnalysisContext(
      new AnalysisContext(),
      loadedRoute,
      orphan = false
    )

    UnexpectedNodeRouteAnalyzer.analyze(context)
  }

}