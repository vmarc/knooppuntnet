package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue214_MultipleNetworkTypes extends UnitTest {

  test("route=hiking;mtb") {
    val route = CaseStudy.routeAnalysis("13328443").route
    route.facts should equal(Seq.empty)
  }
}
