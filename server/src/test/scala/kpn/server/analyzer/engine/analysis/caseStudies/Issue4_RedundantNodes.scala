package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.core.util.UnitTest

class Issue4_RedundantNodes extends UnitTest {

  test("route 21-70") {
    val context = CaseStudy.analyze("3792006")
    context.facts should equal(Seq.empty)
  }

  test("route 24-32") {

    val context = CaseStudy.analyze("3330377")

    context.routeNodesAnalysis.startNode.map(_.name) should equal(Some("24"))
    context.routeNodesAnalysis.endNode.map(_.name) should equal(Some("32"))
    context.routeNodesAnalysis.redundantNodes.map(_.name).sorted should equal(
      Seq(
        "83",
        "84",
        "85",
        "86"
      )
    )

    context.facts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteRedundantNodes, Fact.RouteBroken))
  }

  test("route 56-58") {

    val context = CaseStudy.analyze("3715798")

    context.routeNodesAnalysis.startNode.map(_.name) should equal(Some("56"))
    context.routeNodesAnalysis.endNode.map(_.name) should equal(Some("58"))
    context.routeNodesAnalysis.redundantNodes.map(_.name).sorted should equal(
      Seq(
        "03",
        "04",
        "07",
        "08",
        "24"
      )
    )

    context.facts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteRedundantNodes, Fact.RouteBroken))
  }
}
