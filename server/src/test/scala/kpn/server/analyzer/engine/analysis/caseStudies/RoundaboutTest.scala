package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class RoundaboutTest extends UnitTest {

  test("roundabout in the middle") {
    pending // old tagging is not supported anymore
    val analysis = CaseStudy.routeAnalysis("1193198", oldTagging = true)
    analysis.route.facts shouldBe empty
  }
}
