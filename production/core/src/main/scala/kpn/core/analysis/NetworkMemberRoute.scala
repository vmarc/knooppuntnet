package kpn.core.analysis

import kpn.core.engine.analysis.route.RouteAnalysis
import kpn.shared.common.Ref

case class NetworkMemberRoute(routeAnalysis: RouteAnalysis, role: Option[String]) {

  def id: Long = routeAnalysis.route.id

  def toRef: Ref = Ref(id, routeAnalysis.name)
}
