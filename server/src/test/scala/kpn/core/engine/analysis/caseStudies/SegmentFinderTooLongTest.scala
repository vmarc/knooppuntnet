package kpn.core.engine.analysis.caseStudies

import kpn.shared.Fact
import org.scalatest.FunSuite
import org.scalatest.Matchers

class SegmentFinderTooLongTest extends FunSuite with Matchers {

  test("SegmentFinder timeout") {
    val route = CaseStudy.routeAnalysis("2655355").route
    route.facts should equal(Seq(Fact.RouteAnalysisFailed, Fact.RouteBroken))
  }
}
