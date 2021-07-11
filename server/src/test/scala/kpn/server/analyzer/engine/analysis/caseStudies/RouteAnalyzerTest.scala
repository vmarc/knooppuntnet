package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class RouteAnalyzerTest extends UnitTest {

  test("route name does not match nodes") {
    val analysis = CaseStudy.routeAnalysis("12153045")
    analysis.route.facts shouldBe empty

    analysis.route.summary.name should equal("")
    analysis.route.summary.nodeNames should equal(Seq.empty)

    analysis.routeNodeAnalysis.startNodes should equal(
      Seq.empty
    )

    analysis.routeNodeAnalysis.endNodes should equal(
      Seq.empty
    )

  }
}
