package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.route.RouteNetworkNodeInfo
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

    route.analysis.map.freeNodes should matchTo {
      Seq(
        RouteNetworkNodeInfo(
          47452329L,
          "04",
          "04",
          "52.8022502",
          "4.7589098"
        )
      )
    }
    route.analysis.map.freePaths.size should equal(1)
    val path = route.analysis.map.freePaths.head
    path.startNodeId should equal(47452329L)
    path.endNodeId should equal(47452329L)
    assert(path.meters > 7000)
  }

  test("route 30-30, not a loop but all nodes have the same name") {
    val route = CaseStudy.routeAnalysis("9432838").route
    route.facts shouldBe empty
  }
}
