package kpn.core.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.Matchers

import kpn.shared.Fact.RouteNotBackward
import kpn.shared.Fact.RouteNotForward
import kpn.shared.Fact.RouteNotContinious

class CycleWayOppositeTest extends FunSuite with Matchers {

  test("cycleway=opposite cancels oneway=true") {

    val route = CaseStudy.routeAnalysis("535487").route

    route.facts.contains(RouteNotBackward) should equal(false)
    route.facts.contains(RouteNotForward) should equal(false)
    route.facts.contains(RouteNotContinious) should equal(false)
  }
}
