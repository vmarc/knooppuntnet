package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.Matchers

import kpn.shared.Fact

class RouteInvalidSortingOrderTest extends FunSuite with Matchers {
  test("route with roundabout which is split in multiple fragments: roundabout used only once when checking sorting order") {
    val route = CaseStudy.routeAnalysis("10779").route
    route.facts should equal(Seq())
  }

  test("route with wrong sorting order") {
    val route = CaseStudy.routeAnalysis("119410").route
    route.facts.contains(Fact.RouteInvalidSortingOrder) should equal(true)
  }
}
