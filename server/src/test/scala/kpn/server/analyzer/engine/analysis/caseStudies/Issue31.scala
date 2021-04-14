package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue31 extends UnitTest {

  test("oneway:bicycle=no overrules junction=roundabout oneway") {
    val route = CaseStudy.routeAnalysis("4271").route
    route.facts shouldBe empty // no more RouteNotBackward etc. generated
  }

}
