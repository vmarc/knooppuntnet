package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue114_LoopRoute extends UnitTest {

  test("route 62-62") {
    val route = CaseStudy.routeAnalysis("11772920").route
    route.facts shouldBe empty
    println(route.analysis.structureStrings)
  }

  test("route 4-4") {
    val route = CaseStudy.routeAnalysis("11659448").route
    route.facts shouldBe empty
  }

  test("route 30-30, not a loop but all nodes have the same name") {
    val route = CaseStudy.routeAnalysis("9432838").route
    route.facts shouldBe empty
  }
}
