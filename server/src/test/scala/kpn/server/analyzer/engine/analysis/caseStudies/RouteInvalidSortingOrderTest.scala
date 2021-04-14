package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class RouteInvalidSortingOrderTest extends UnitTest {
  test("route with roundabout which is split in multiple fragments: roundabout used only once when checking sorting order") {
    val route = CaseStudy.routeAnalysis("10779").route
    route.facts shouldBe empty
  }

  test("route with wrong sorting order") {
    val route = CaseStudy.routeAnalysis("119410").route
    route.facts should contain(Fact.RouteInvalidSortingOrder)
  }
}
