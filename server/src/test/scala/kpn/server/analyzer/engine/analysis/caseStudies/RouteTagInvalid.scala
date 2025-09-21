package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.api.common.Fact.RouteWithoutNodes
import kpn.core.util.UnitTest

class RouteTagInvalid extends UnitTest {

  test("mismatch between route=hiking and network=lcn (cycling)") {
    val route = CaseStudy.baseRouteDoc("10993501")
    route.facts should equal(Seq(Fact.RouteTagInvalid, RouteWithoutNodes, Fact.RouteBroken))
  }
}
