package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class WithoutTiles extends UnitTest {

  test("problem introduced with SplitNodeRouteAnalyzer is gone") {
    val analysis = CaseStudy.routeAnalysis("11525591")
    analysis.route.tiles.isEmpty should equal(false)
  }
}
