package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue109_RoundaboutRoute extends UnitTest {

  test("route that consists of a single roundabout with 9 ways connecting 8 nodes with the same name") {

    val context = CaseStudy.analyze("11512870")

    context.routeNameAnalysis.name should equal(Some("45-45"))

    context.routeNodesAnalysis.startNode.map(_.alternateName) should equal(Some("45"))
    context.routeNodesAnalysis.endNode.map(_.alternateName) should equal(Some("45.a"))
    context.routeNodesAnalysis.endTentacleNodes.map(_.alternateName) should equal(
      Seq(
        "45.b",
        "45.c",
        "45.d",
        "45.e",
        "45.f",
        "45.g"
      )
    )
    pendingRedesignLoop()
    context.facts shouldBe empty
  }
}
