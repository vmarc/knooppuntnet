package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class Issue377_RouteNodeNameMismatch extends UnitTest {

  test("RouteNodeNameMismatch") {
    val route = CaseStudy.routeAnalysis("13945193").route
    route.facts should equal(
      Seq(
        Fact.RouteRedundantNodes,
        Fact.RouteUnusedSegments,
        Fact.RouteBroken
      )
    )
  }
}
