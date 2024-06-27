package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class IssueRouteNameTest extends UnitTest {

  test("note") {
    val analysis = CaseStudy.routeAnalysis("14755555")
    analysis.routeDetail.summary.name should equal("Chemin de la Grive")
    analysis.routeDetail.facts.shouldMatchTo(Seq(Fact.RouteNodeNameMismatch))
  }
}
