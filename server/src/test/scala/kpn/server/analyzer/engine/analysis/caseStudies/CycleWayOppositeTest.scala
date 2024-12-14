package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.core.util.UnitTest

class CycleWayOppositeTest extends UnitTest {

  test("cycleway=opposite cancels oneway=true") {

    val route = CaseStudy.routeDetailDoc("535487")

    assert(!route.facts.contains(Fact.RouteNotBackward))
    assert(!route.facts.contains(Fact.RouteNotForward))
    assert(!route.facts.contains(Fact.RouteNotContinious))
  }
}
