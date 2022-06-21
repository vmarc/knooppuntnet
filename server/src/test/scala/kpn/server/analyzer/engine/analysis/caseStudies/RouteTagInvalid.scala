package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class RouteTagInvalid extends UnitTest {

  test("mismatch between route=hiking and network=lcn (cycling)") {
    val route = CaseStudy.routeAnalysis("10993501").route
    route.facts should equal(Seq(Fact.RouteTagInvalid, Fact.RouteWithoutNodes, Fact.RouteBroken))
  }

}
