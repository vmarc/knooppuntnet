package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class CanoeRouteAccessibilityTest extends UnitTest {
  test("canoe route accessible") {
    val route = CaseStudy.routeDetailDoc("8473146")
    assertEqual(route.facts, Seq.empty)
  }
}
