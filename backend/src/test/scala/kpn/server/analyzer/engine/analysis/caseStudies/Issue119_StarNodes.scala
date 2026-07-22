package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue119_StarNodes extends UnitTest {

  test("route 90-91") {
    val context = CaseStudy.analyze("11838989")
    context.facts shouldBe empty
  }
}
