package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue203_NamedRoutes extends UnitTest {

  test("Carrefour des Planches - Croix des Clos") {
    val route = CaseStudy.routeAnalysis("13305500").route
    route.facts shouldBe empty
    println(route.analysis.structureStrings)
  }
}
