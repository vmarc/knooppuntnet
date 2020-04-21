package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import kpn.api.custom.Fact.RouteNodeNameMismatch

class RouteNodeNameMismatchTest extends AnyFunSuite with Matchers {

  test("2677068") {
    val route = CaseStudy.routeAnalysis("2677068").route
    route.facts.contains(RouteNodeNameMismatch) should equal(true)
  }
}
