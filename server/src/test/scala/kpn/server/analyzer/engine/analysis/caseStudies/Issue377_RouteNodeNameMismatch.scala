package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.core.util.UnitTest

class Issue377_RouteNodeNameMismatch extends UnitTest {

  test("RouteNodeNameMismatch") {
    val route = CaseStudy.baseRouteDoc("13945193")
    route.facts should equal(
      Seq(
        Fact.RouteRedundantNodes,
        Fact.RouteUnusedSegments,
        Fact.RouteBroken
      )
    )
  }

  test("route with RouteNodeNameMismatch") {
    val route = CaseStudy.baseRouteDoc("12347801")
    route.facts should equal(Seq(Fact.RouteNodeNameMismatch))
  }
}
