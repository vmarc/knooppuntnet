package kpn.core.planner

import kpn.shared.SharedTestObjects
import kpn.shared.route.RouteInfo
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DistanceCalculatorTest extends FunSuite with Matchers with SharedTestObjects {

  test("cumalutive distances are derived from route lengths") {
    val routeMap: Map[Long, RouteInfo] = Map(
      1L -> newRoute(meters = 300),
      2L -> newRoute(meters = 700)
    )
    val encodedPlan = EncodedPlan("N1-R1-N2-R2-N3")
    new DistanceCalculator(routeMap).calculateDistances(encodedPlan) should equal(Seq(0, 300, 300, 1000, 1000))
  }

  test("empty plan results in empty distance collection") {
    val routeMap: Map[Long, RouteInfo] = Map.empty
    val encodedPlan = EncodedPlan("")
    new DistanceCalculator(routeMap).calculateDistances(encodedPlan) should equal(Seq())
  }
}
