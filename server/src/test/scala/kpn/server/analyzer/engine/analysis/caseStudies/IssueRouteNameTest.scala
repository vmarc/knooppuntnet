package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact.RouteNodeNameMismatch
import kpn.core.util.UnitTest

class IssueRouteNameTest extends UnitTest {

  test("note") {
    val analysis = CaseStudy.routeAnalysis("14755555")
    analysis.route.summary.name should equal("Chemin de la Grive")
    analysis.route.facts.shouldMatchTo(Seq(RouteNodeNameMismatch))
  }
}
