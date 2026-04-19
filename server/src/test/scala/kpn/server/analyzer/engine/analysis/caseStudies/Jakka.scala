package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Jakka extends UnitTest {

  test("route 04-07 unused segments") {
    val route = CaseStudy.routeAnalysis("1432559").route
    route.facts.shouldMatchTo(Seq.empty)
  }
}

