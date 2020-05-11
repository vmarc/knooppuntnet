package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNameMissing
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.api.custom.Fact.RouteRedundantNodes
import kpn.core.util.UnitTest

class ManyNodesWithSameNameInRouteTest extends UnitTest {

  test("route with 27 times a node with the same name") {
    val route = CaseStudy.routeAnalysis("10887712").route
    route.facts.toSet should equal(
      Set(
        RouteNameMissing,
        RouteRedundantNodes,
        RouteNotForward,
        RouteNotBackward,
        RouteNotContinious,
        RouteBroken
      )
    )
  }
}
