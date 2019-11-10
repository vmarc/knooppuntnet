package kpn.core.analysis

import kpn.api.common.common.Ref
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis

case class NetworkMemberRoute(routeAnalysis: RouteAnalysis, role: Option[String]) {

  def id: Long = routeAnalysis.route.id

  def toRef: Ref = Ref(id, routeAnalysis.name)
}
