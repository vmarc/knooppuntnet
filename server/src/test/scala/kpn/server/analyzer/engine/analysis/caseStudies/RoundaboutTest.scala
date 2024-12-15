package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact.RouteNameDeprecatedNoteTag
import kpn.core.util.UnitTest

class RoundaboutTest extends UnitTest {

  test("roundabout in the middle") {
    val route = CaseStudy.routeDetailDoc("1193198")
    assertEqual(
      route.facts,
      Seq(RouteNameDeprecatedNoteTag)
    )
  }
}
