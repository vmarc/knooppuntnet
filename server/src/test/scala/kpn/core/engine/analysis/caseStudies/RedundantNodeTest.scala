package kpn.core.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.Matchers

import kpn.shared.Fact.RouteNotBackward
import kpn.shared.Fact.RouteNotContinious
import kpn.shared.Fact.RouteNotForward
import kpn.shared.Fact.RouteRedundantNodes

class RedundantNodeTest extends FunSuite with Matchers {

  test("redundant node in the middle should not prevent forward and backward path calculation") {

    val route = CaseStudy.routeAnalysis("2614657").route

    route.facts.contains(RouteRedundantNodes) should equal(true)

    route.facts.contains(RouteNotBackward) should equal(false)
    route.facts.contains(RouteNotForward) should equal(false)
    route.facts.contains(RouteNotContinious) should equal(false)
  }
}
