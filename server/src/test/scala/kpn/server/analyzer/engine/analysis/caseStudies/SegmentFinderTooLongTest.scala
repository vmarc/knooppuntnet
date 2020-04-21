package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class SegmentFinderTooLongTest extends FunSuite with Matchers {

  test("SegmentFinder timeout") {
    val route = CaseStudy.routeAnalysis("2655355").route
    route.facts should equal(Seq(Fact.RouteAnalysisFailed, Fact.RouteBroken))
  }
}
