package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue114_LoopRoute extends UnitTest {

  test("route 62-62") {
    val context = CaseStudy.analyze("11772920")
    context.facts shouldBe empty
  }

  test("route 4-4") {
    pending // TODO redesign
    val context = CaseStudy.analyze("11659448")

    context.facts shouldBe empty

    //    context.nodes.redundantNodes.shouldMatchTo {
    //      Seq(
    //        RouteNode(
    //          47452329L,
    //          "52.8022502",
    //          "4.7589098",
    //          "04",
    //          "04",
    //          isInWay = false
    //        )
    //      )
    //    }
    //    context.paths.size should equal(1)
    //    val path = context.paths.head
    //    path.startNodeId should equal(47452329L)
    //    path.endNodeId should equal(47452329L)
    //    assert(path.meters > 7000)
  }

  test("route 30-30, not a loop but all nodes have the same name") {
    val context = CaseStudy.analyze("9432838")
    context.facts shouldBe empty
  }
}
