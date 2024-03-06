package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact.RouteNameDeprecatedNoteTag
import kpn.core.util.UnitTest

class RoundaboutTest extends UnitTest {

  test("roundabout in the middle") {
    val analysis = CaseStudy.routeAnalysis("1193198")
    analysis.route.facts.shouldMatchTo(Seq(RouteNameDeprecatedNoteTag))
  }
}
