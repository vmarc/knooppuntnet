package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.core.util.UnitTest

class CycleWayOppositeTest extends UnitTest {

  test("cycleway=opposite cancels oneway=true") {

    val route = CaseStudy.routeAnalysis("535487").route

    assert(!route.facts.contains(RouteNotBackward))
    assert(!route.facts.contains(RouteNotForward))
    assert(!route.facts.contains(RouteNotContinious))
  }
}
