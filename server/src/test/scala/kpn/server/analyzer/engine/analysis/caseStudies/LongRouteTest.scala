package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class LongRouteTest extends UnitTest {

  test("process large route") {
    val route = CaseStudy.routeAnalysis("11109600").route
    route.facts shouldBe empty
  }
}
