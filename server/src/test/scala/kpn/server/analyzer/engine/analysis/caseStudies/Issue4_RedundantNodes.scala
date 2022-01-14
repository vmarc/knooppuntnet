package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class Issue4_RedundantNodes extends UnitTest {

  test("route 21-70") {
    val route = CaseStudy.routeAnalysis("3792006").route
    route.facts should equal(Seq.empty)
  }

  test("route 24-32") {

    val route = CaseStudy.routeAnalysis("3330377").route

    route.analysis.map.startNodes.map(_.name).sorted should equal(Seq("24"))
    route.analysis.map.endNodes.map(_.name).sorted should equal(Seq("32"))
    route.analysis.map.redundantNodes.map(_.name).sorted should equal(
      Seq(
        "83",
        "84",
        "85",
        "86"
      )
    )

    route.facts should equal(Seq(Fact.RouteRedundantNodes, Fact.RouteBroken))
  }

  test("route 56-58") {

    val route = CaseStudy.routeAnalysis("3715798").route

    route.analysis.map.startNodes.map(_.name).sorted should equal(Seq("56"))
    route.analysis.map.endNodes.map(_.name).sorted should equal(Seq("58"))
    route.analysis.map.redundantNodes.map(_.name).sorted should equal(
      Seq(
        "03",
        "04",
        "07",
        "08",
        "24"
      )
    )

    route.facts should equal(Seq(Fact.RouteRedundantNodes, Fact.RouteBroken))
  }
}
