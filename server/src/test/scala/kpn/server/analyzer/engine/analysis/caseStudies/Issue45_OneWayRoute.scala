package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class Issue45_OneWayRoute extends UnitTest {

  test("route 60-61") {
    pending // old tagging is not supported anymore
    val route = CaseStudy.routeAnalysis("7328339", oldTagging = true).route
    route.facts should equal(Seq(Fact.RouteOneWay))
  }

  test("route 63-64") {
    pending // old tagging is not supported anymore
    val route = CaseStudy.routeAnalysis("9515132", oldTagging = true).route
    route.facts should equal(Seq(Fact.RouteOneWay))
  }

  test("route 84-86") {
    pending // old tagging is not supported anymore
    val route = CaseStudy.routeAnalysis("6635664", oldTagging = true).route
    route.facts should equal(Seq(Fact.RouteOneWay))
  }

  test("route 74-86") {
    pending // old tagging is not supported anymore
    val route = CaseStudy.routeAnalysis("6635670", oldTagging = true).route
    route.facts should equal(Seq(Fact.RouteOneWay))
  }
}
