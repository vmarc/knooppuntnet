package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class LongRouteTest extends UnitTest {

  test("process large route") {
    val context = CaseStudy.analyze("11109600")
    context.facts.shouldMatchTo(Seq.empty)
  }
}
