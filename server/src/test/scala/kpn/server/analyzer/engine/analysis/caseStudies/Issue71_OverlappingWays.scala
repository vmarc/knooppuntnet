package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue71_OverlappingWays extends UnitTest {

  test("route 17-19") {
    val route = CaseStudy.routeAnalysis("1245740").route
    route.facts should equal(Seq.empty)
  }

  test("route 314-353") {
    val route = CaseStudy.routeAnalysis("9637368").route
    route.facts should equal(Seq.empty)
  }

  test("route 01-41") {
    val route = CaseStudy.routeAnalysis("10015252").route
    route.facts should equal(Seq.empty)
  }

  test("route 93-95") {
    val route = CaseStudy.routeAnalysis("145281").route
    route.facts should equal(Seq.empty)
  }
}
