package kpn.core.analysis

import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.shared.common.Ref

case class NetworkMemberRoute(routeAnalysis: RouteAnalysis, role: Option[String]) {

  def id: Long = routeAnalysis.route.id

  def toRef: Ref = Ref(id, routeAnalysis.name)
}
