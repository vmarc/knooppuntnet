package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.core.util.UnitTest

class Issue126_CircularRoute extends UnitTest {

  test("route 75-75") {

    // [date:"2020-11-30T00:00:00Z"];relation(11858847);(>>;);out meta;
    val route = CaseStudy.routeAnalysis("11858847").route

    route.facts shouldBe empty

    route.analysis.map.freeNodes.shouldMatchTo {
      Seq(
        RouteNetworkNodeInfo(
          43052537L,
          "75",
          "75",
          None,
          "51.5672438",
          "3.7800625"
        )
      )
    }

    route.analysis.map.freePaths.size should equal(1)

    val path = route.analysis.map.freePaths.head
    path.startNodeId should equal(43052537L)
    path.endNodeId should equal(43052537L)
    path.segments.size should equal(3)

    route.analysis.structureStrings should equal(
      Seq(
        "free=(75-75 via +<75- 868375685>+<868375684>-+<868375682>-+<7608907>+<7608890>+<-75 7608893>)"
      )
    )
  }
}
