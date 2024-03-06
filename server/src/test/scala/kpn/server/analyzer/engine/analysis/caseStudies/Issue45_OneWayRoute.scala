package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class Issue45_OneWayRoute extends UnitTest {

  test("route 60-61") {
    val route = CaseStudy.routeAnalysis("7328339").route
    route.facts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
  }

  test("route 63-64") {
    val route = CaseStudy.routeAnalysis("9515132").route
    route.facts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
  }

  test("route 84-86") {
    val route = CaseStudy.routeAnalysis("6635664").route
    route.facts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
  }

  test("route 74-86") {
    val route = CaseStudy.routeAnalysis("6635670").route
    route.facts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
  }
}
