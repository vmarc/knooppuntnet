package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class CanoeRouteAccessibilityTest extends UnitTest {
  test("canoe route accessible") {
    pending // old tagging is not supported anymore
    val route = CaseStudy.routeAnalysis("8473146", oldTagging = true).route
    route.facts shouldBe empty
  }
}
