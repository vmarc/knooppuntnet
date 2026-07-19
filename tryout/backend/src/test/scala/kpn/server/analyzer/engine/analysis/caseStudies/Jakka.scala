package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.core.util.UnitTest

class Jakka extends UnitTest {

  test("route 04-07 unused segments") {
    val context = CaseStudy.analyze("1432559")
    context.facts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
  }
}
