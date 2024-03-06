package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class CycleWayOppositeTest extends UnitTest {

  test("cycleway=opposite cancels oneway=true") {

    val route = CaseStudy.routeAnalysis("535487").route

    assert(!route.facts.contains(Fact.RouteNotBackward))
    assert(!route.facts.contains(Fact.RouteNotForward))
    assert(!route.facts.contains(Fact.RouteNotContinious))
  }
}
