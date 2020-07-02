package kpn.server.api.planner.leg

import kpn.api.common.planner.LegEnd

object PlanKey {

  def leg(source: LegEnd, sink: LegEnd): String = {
    s"${legEnd(source)}-${legEnd(sink)}"
  }

  def legEnd(legEnd: LegEnd): String = {
    legEnd.node match {
      case Some(legEndNode) => legEndNode.nodeId.toString
      case None =>
        legEnd.route match {
          case Some(legEndRoute) => s"${legEndRoute.routeId}.${legEndRoute.pathId}"
          case None => ""
        }
    }
  }

}
