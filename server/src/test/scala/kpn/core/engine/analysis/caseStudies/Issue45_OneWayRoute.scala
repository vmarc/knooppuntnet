package kpn.core.engine.analysis.caseStudies

import kpn.shared.Fact
import org.scalatest.FunSuite
import org.scalatest.Matchers

class Issue45_OneWayRoute extends FunSuite with Matchers {

  test("route 60-61") {
    val route = CaseStudy.routeAnalysis("7328339").route
    route.facts should equal(Seq(Fact.RouteOneWay))
  }

  test("route 63-64") {
    val route = CaseStudy.routeAnalysis("9515132").route
    route.facts should equal(Seq(Fact.RouteOneWay))
  }

  test("route 84-86") {
    val route = CaseStudy.routeAnalysis("6635664").route
    route.facts should equal(Seq(Fact.RouteOneWay))
  }

  test("route 74-86") {
    val route = CaseStudy.routeAnalysis("6635670").route
    route.facts should equal(Seq(Fact.RouteOneWay))
  }

}
