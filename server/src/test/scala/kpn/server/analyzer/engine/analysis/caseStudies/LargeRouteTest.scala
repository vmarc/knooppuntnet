package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class LargeRouteTest extends UnitTest {

  test("Super large route") {
    val context = CaseStudy.analyze("222560")
    context.segments.size should equal(321)
  }
}
