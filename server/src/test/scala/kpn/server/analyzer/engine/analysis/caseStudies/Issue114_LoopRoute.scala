package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue114_LoopRoute extends UnitTest {

  test("route 62-62") {
    val route = CaseStudy.routeAnalysis("11772920").route
    route.facts should equal(Seq())
    println(route.analysis.structureStrings)
  }

  test("route 4-4") {
    val route = CaseStudy.routeAnalysis("11659448").route
    route.facts should equal(Seq())
  }

}
