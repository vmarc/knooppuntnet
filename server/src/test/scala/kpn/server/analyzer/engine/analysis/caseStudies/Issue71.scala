package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class Issue71 extends UnitTest {

  test("route 17-19") {
    val route = CaseStudy.routeAnalysis("9637368").route
    route.facts should equal(Seq(Fact.RouteUnusedSegments, Fact.RouteInvalidSortingOrder, Fact.RouteBroken))
  }

  test("route 314-353") {
    val route = CaseStudy.routeAnalysis("1245740").route
    route.facts should equal(Seq(Fact.RouteInvalidSortingOrder))
  }

}
