package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class SegmentFinderTooLongTest extends UnitTest {

  test("SegmentFinder timeout") {
    val route = CaseStudy.routeAnalysis("2655355").route
    // RouteAnalysisFailed is not produced anymore after performance improvements (no longer timeout)
    route.facts should equal(Seq(Fact.RouteUnusedSegments, Fact.RouteInvalidSortingOrder, Fact.RouteBroken))
  }
}
