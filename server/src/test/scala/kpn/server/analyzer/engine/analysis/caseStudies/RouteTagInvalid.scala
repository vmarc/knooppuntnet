package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.core.util.UnitTest

class RouteTagInvalid extends UnitTest {

  test("mismatch between route=hiking and network=lcn (cycling)") {
    val route = CaseStudy.routeDetailDoc("10993501")
    route.facts should equal(Seq(Fact.RouteTagInvalid, Fact.RouteBroken))
  }
}
