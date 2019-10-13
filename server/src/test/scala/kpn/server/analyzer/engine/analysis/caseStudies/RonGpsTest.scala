package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.Matchers

/**
 * Verifies a problem reported by RonGps.
 * <p>
 * Before the problem was fixed, the analysis logic assumed that the ways of the
 * route in the forward direction (from low numbered node to high numbered node)
 * came before the ways of the route in backward direction (high to low). In this
 * route the order is high-to-low first and then low-to-high.
 * <p>
 * The original logic reported 'RouteBroken' for 'RonGpsOriginal', the current logic does
 * no longer see a problem with the route.
 */
class RonGpsTest extends FunSuite with Matchers {

  test("RonGps original definition is reported to be broken") {
    val route = CaseStudy.routeAnalysis("RonGpsOriginal").route
    route.facts should be(empty)
  }

  test("ways for lower to higher branch moved to front, before ways for higher to lower branch") {
    val route = CaseStudy.routeAnalysis("RonGpsFixed").route
    route.facts should be(empty)
  }

  test("all forward roles changed to backward - oneway ways are followed in the wrong direction") {
    val route = CaseStudy.routeAnalysis("RonGpsBackward").route
    route.facts should be(empty)
  }
}
