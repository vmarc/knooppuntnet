package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.core.util.UnitTest

class IssueRouteNameTest extends UnitTest {

  test("note") {
    val route = CaseStudy.routeDetailDoc("14755555")
    route.summary.name should equal("Chemin de la Grive")
    assertEqual(
      route.facts,
      Seq(Fact.RouteNodeNameMismatch)
    )
  }
}
