package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class LargeRouteTest extends UnitTest {

  ignore("Super large route") {
    val analysis = CaseStudy.routeAnalysis("222560")
    analysis.route.analysis.get.map.unusedSegments.size should equal(200)
  }
}
