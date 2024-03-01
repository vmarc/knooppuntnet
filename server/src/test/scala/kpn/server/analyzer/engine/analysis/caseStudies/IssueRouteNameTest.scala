package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class IssueRouteNameTest extends UnitTest {

  test("note") {
    val analysis = CaseStudy.routeAnalysis("14755555")
    analysis.route.summary.name should equal("Chemin de la Grive")
    analysis.route.facts.size should equal(0)
  }
}
