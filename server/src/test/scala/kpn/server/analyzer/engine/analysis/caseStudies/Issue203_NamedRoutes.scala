package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.core.util.UnitTest

class Issue203_NamedRoutes extends UnitTest {

  test("Carrefour des Planches - Croix des Clos") {
    val context = CaseStudy.analyze("13305500")
    context.facts should equal(
      Seq(
        Fact.RouteRedundantNodes,
        Fact.RouteNotForward,
        Fact.RouteNotBackward,
        Fact.RouteNotContinious,
        Fact.RouteBroken
      )
    )
  }

  test("Le Villard - Le Villard") {
    val context = CaseStudy.analyze("12219285")
    context.facts should equal(
      Seq(
        Fact.RouteRedundantNodes,
        Fact.RouteUnusedSegments,
        Fact.RouteBroken
      )
    )
  }
}
