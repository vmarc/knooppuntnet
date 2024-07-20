package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.Redesign
import kpn.core.util.UnitTest

class Issue379_RedundantNodes extends UnitTest {

  test("analyze route with redundant nodes") {
    val route = CaseStudy.routeDetailDoc("17574316")
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
