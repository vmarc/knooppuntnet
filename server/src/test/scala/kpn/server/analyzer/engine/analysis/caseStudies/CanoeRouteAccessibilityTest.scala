package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class CanoeRouteAccessibilityTest extends UnitTest {
  test("canoe route accessible") {
    val route = CaseStudy.routeAnalysis("8473146").route
    route.facts.shouldMatchTo(Seq.empty)
  }
}
