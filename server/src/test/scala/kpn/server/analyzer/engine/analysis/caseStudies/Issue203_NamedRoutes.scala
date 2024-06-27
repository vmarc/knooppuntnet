package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.Redesign
import kpn.core.util.UnitTest

class Issue203_NamedRoutes extends UnitTest {

  test("Carrefour des Planches - Croix des Clos") {
    val route = CaseStudy.routeAnalysis("13305500").routeDetail
    route.oldFacts should equal(
      Seq(
        Fact.RouteRedundantNodes,
        Fact.RouteNotForward,
        Fact.RouteNotBackward,
        Fact.RouteNotContinious,
        Fact.RouteBroken
      )
    )
    if (Redesign.enableNewFactTests) {
      route.facts should equal(
        Seq(
          Fact.RouteRedundantNodes,
          Fact.RouteNotForward,
          Fact.RouteNotBackward,
          Fact.RouteNotContinious,
          Fact.RouteBroken
        )
      )
    }
  }

  test("Le Villard - Le Villard") {
    val route = CaseStudy.routeAnalysis("12219285").routeDetail
    route.oldFacts should equal(
      Seq(
        Fact.RouteRedundantNodes,
        Fact.RouteUnusedSegments,
        Fact.RouteBroken
      )
    )
    if (Redesign.enableNewFactTests) {
      route.facts should equal(
        Seq(
          Fact.RouteRedundantNodes,
          Fact.RouteUnusedSegments,
          Fact.RouteBroken
        )
      )
    }
  }
}
