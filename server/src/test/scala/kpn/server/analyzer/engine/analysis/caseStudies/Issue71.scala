package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class Issue71 extends FunSuite with Matchers {

  test("route 17-19") {
    val route = CaseStudy.routeAnalysis("9637368").route
    route.facts should equal(Seq(Fact.RouteUnusedSegments, Fact.RouteInvalidSortingOrder, Fact.RouteBroken))
  }

  test("route 314-353") {
    val route = CaseStudy.routeAnalysis("1245740").route
    route.facts should equal(Seq(Fact.RouteInvalidSortingOrder))
  }

}
