package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact.RouteNodeNameMismatch
import kpn.core.util.UnitTest

class RouteNodeNameMismatchTest extends UnitTest {

  test("2677068") {
    val route = CaseStudy.routeAnalysis("2677068").route
    route.facts.contains(RouteNodeNameMismatch) should equal(true)
  }
}
