package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteWithoutWays
import kpn.api.common.data.MemberType
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newRelation
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData

class BaseRouteWithoutWaysAnalyzerTest extends UnitTest {

  test("route relation contains way(s)") {

    val d = new RouteTestData("01-02") {
      node(1001, "01")
      node(1002, "02")
      memberWay(101L, "", 1001, 1002)
    }

    val context = analyze(d)

    assertEqual(context.facts, Seq.empty)
  }

  test("route relation without way members") {
    val d = new RouteTestData("01-02")
    val context = analyze(d)
    assertEqual(context.facts, Seq(RouteWithoutWays))
  }

  test("no RouteWithoutWays fact in a superroute") {
    val relation = newRelation(tags = Tags.from("type" -> "superroute"))
    val context = BaseRouteAnalysisContext(relation, None)
    assertEqual(BaseRouteWithoutWaysAnalyzer.analyze(context).facts, Seq.empty)
  }

  test("no RouteWithoutWays when there are subrelations") {
    val d = new RouteTestData("01-02") {
      relation(2)
      member(MemberType.Relation, 2)
    }
    val context = analyze(d)
    assertEqual(context.facts, Seq.empty)
  }

  private def analyze(routeTestData: RouteTestData): BaseRouteAnalysisContext = {
    val relation = routeTestData.data.relations(1L)
    val context = BaseRouteAnalysisContext(relation, None)
    BaseRouteWithoutWaysAnalyzer.analyze(context)
  }
}
