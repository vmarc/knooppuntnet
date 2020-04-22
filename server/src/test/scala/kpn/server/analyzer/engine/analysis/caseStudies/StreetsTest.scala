package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class StreetsTest extends UnitTest {

  test("find street names in ways") {
    val analysis = CaseStudy.routeAnalysis("1029885")
    analysis.route.analysis.get.map.streets should equal(Seq("Laagheideweg", "Lorbaan"))
  }
}
