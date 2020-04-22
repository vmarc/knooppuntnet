package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class SegmentFinderTooLongTest extends UnitTest {

  test("SegmentFinder timeout") {
    val route = CaseStudy.routeAnalysis("2655355").route
    route.facts should equal(Seq(Fact.RouteAnalysisFailed, Fact.RouteBroken))
  }
}
