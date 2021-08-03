package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue31 extends UnitTest {

  test("oneway:bicycle=no overrules junction=roundabout oneway") {
    pending // old tagging is not supported anymore
    val route = CaseStudy.routeAnalysis("4271", oldTagging = true).route
    route.facts shouldBe empty // no more RouteNotBackward etc. generated
  }

}
