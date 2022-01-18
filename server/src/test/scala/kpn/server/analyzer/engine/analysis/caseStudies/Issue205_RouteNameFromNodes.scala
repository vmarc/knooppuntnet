package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue205_RouteNameFromNodes extends UnitTest {

  test("route Golf - Golf") {
    val route = CaseStudy.routeAnalysis("13331398").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("Golf - Golf")
    route.analysis.map.freePaths.size should equal(1)
    route.analysis.map.freeNodes.map(_.name) should equal(Seq("Golf"))
  }

  test("route ?-? instead of no-name") {
    val route = CaseStudy.routeAnalysis("13669113").route
    route.facts should equal(Seq.empty)
    route.summary.name should equal("?-?")
  }
}
