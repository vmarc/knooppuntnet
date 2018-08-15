package kpn.core.planner

import kpn.shared.route.RouteInfo

class DistanceCalculator(routeMap: Map[Long, RouteInfo]) {
  def calculateDistances(encodedPlan: EncodedPlan): Seq[Int] = {
    encodedPlan.items.map {
      case r: EncodedRoute =>
        routeMap.get(r.routeId) match {
          case Some(route) => route.summary.meters
          case _ => 0
        }
      case _ => 0
    }.scanLeft(0)(_ + _).tail
  }
}
