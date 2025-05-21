package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue126_CircularRoute extends UnitTest {

  test("route 75-75") {

    // [date:"2020-11-30T00:00:00Z"];relation(11858847);(>>;);out meta;
    val context = CaseStudy.analyze("11858847")

    pendingRedesignLoop()
    context.facts shouldBe empty

    //    context.analysis.map.freeNodes.shouldMatchTo {
    //      Seq(
    //        RouteNetworkNodeInfo(
    //          43052537L,
    //          "75",
    //          "75",
    //          None,
    //          "51.5672438",
    //          "3.7800625"
    //        )
    //      )
    //    }
    //
    //    context.analysis.map.freePaths.size should equal(1)
    //
    //    val path = context.analysis.map.freePaths.head
    //    path.startNodeId should equal(43052537L)
    //    path.endNodeId should equal(43052537L)
    //    path.segments.size should equal(3)
    //
    //    context.analysis.structureStrings should equal(
    //      Seq(
    //        "free=(75-75 via +<75- 868375685>+<868375684>-+<868375682>-+<7608907>+<7608890>+<-75 7608893>)"
    //      )
    //    )
  }
}
