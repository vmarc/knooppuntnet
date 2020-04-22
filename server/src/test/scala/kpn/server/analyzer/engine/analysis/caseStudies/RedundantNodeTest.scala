package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.api.custom.Fact.RouteRedundantNodes
import kpn.core.util.UnitTest

class RedundantNodeTest extends UnitTest {

  test("redundant node in the middle should not prevent forward and backward path calculation") {

    val route = CaseStudy.routeAnalysis("2614657").route

    route.facts.contains(RouteRedundantNodes) should equal(true)

    route.facts.contains(RouteNotBackward) should equal(false)
    route.facts.contains(RouteNotForward) should equal(false)
    route.facts.contains(RouteNotContinious) should equal(false)
  }
}
