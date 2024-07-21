package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue31 extends UnitTest {

  test("oneway:bicycle=no overrules junction=roundabout oneway") {
    val route = CaseStudy.routeDetailDoc("4271")
    route.facts shouldBe empty // no more RouteNotBackward etc. generated
  }
}
