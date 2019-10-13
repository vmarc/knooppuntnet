package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.Matchers

import kpn.shared.Fact.RouteNodeNameMismatch

class RouteNodeNameMismatchTest extends FunSuite with Matchers {

  test("2677068") {
    val route = CaseStudy.routeAnalysis("2677068").route
    route.facts.contains(RouteNodeNameMismatch) should equal(true)
  }
}
