package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.common.Fact.RouteWithoutWays
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class WithoutWaysRouteAnalyzerTest extends UnitTest with SharedTestObjects {

  test("route relation contains way(s)") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
      memberWay(101L, "", 1001, 1002)
    }

    val context = analyze(d)

    context.facts.shouldMatchTo(Seq.empty)
  }

  test("route relation without way members") {
    val d = new RouteTestData("01-02")
    val context = analyze(d)
    context.facts.shouldMatchTo(Seq(RouteWithoutWays))
  }

  test("no RouteWithoutWays fact in a superroute") {
    val relation = newRelation(tags = Tags.from("type" -> "superroute"))
    val context = RouteDetailAnalysisContext(relation, None)
    WithoutWaysRouteAnalyzer.analyze(context).facts.shouldMatchTo(Seq.empty)
  }

  test("no RouteWithoutWays when there are subrelations") {
    val d = new RouteTestData("01-02") {
      relation(2)
      member("relation", 2)
    }
    val context = analyze(d)
    context.facts.shouldMatchTo(Seq.empty)
  }

  private def analyze(routeTestData: RouteTestData): RouteDetailAnalysisContext = {
    val relation = routeTestData.data.relations(1L)
    val context = RouteDetailAnalysisContext(relation, None)
    WithoutWaysRouteAnalyzer.analyze(context)
  }
}
