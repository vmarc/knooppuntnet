package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData

class RouteRelationAnalyzerTest extends UnitTest with SharedTestObjects {

  test("ids from nodes in ways") {
    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      memberWay(5, "", 1, 2)
    }
    analyze(d) should equal(Seq(1, 2))
  }

  test("ids from nodes in relation members") {
    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      memberNode(1, "")
      memberNode(2, "")
    }
    analyze(d) should equal(Seq(1, 2))
  }

  test("prefer the position of the node in the ways over the position in the route relation") {
    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      memberNode(2)
      memberNode(1)
      memberWay(5, "", 1, 2)
    }
    analyze(d) should equal(Seq(1, 2))
  }

  private def analyze(d: RouteTestData): Seq[Long] = {
    val relation = d.data.relations(1L)
    val analyzer = new RouteRelationAnalyzer()
    analyzer.orderedNodeIds(relation)
  }

}
