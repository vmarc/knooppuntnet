package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue71_OverlappingWays extends UnitTest {

  test("route 17-19") {
    val context = CaseStudy.analyze("1245740")
    context.facts should equal(Seq.empty)
  }

  test("route 314-353") {
    val context = CaseStudy.analyze("9637368")
    context.facts should equal(Seq.empty)
  }

  test("route 01-41") {
    val context = CaseStudy.analyze("10015252")
    context.facts should equal(Seq.empty)
  }

  test("route 93-95") {
    val context = CaseStudy.analyze("145281")
    context.facts should equal(Seq.empty)
  }
}
