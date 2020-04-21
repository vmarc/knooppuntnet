package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotForward
import kpn.api.custom.Fact.RouteNotContinious

class CycleWayOppositeTest extends FunSuite with Matchers {

  test("cycleway=opposite cancels oneway=true") {

    val route = CaseStudy.routeAnalysis("535487").route

    route.facts.contains(RouteNotBackward) should equal(false)
    route.facts.contains(RouteNotForward) should equal(false)
    route.facts.contains(RouteNotContinious) should equal(false)
  }
}
