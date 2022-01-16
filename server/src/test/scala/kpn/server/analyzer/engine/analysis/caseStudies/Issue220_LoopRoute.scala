package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue220_LoopRoute extends UnitTest {

  test("route A25-A25") {
    val route = CaseStudy.routeAnalysis("11829926").route
    route.facts should equal(Seq.empty)
    route.analysis.map.freePaths.size should equal(1)
    route.analysis.map.freeNodes.map(_.name) should equal(Seq("A25"))
  }
}
