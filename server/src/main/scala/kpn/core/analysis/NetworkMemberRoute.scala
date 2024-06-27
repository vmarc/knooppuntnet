package kpn.core.analysis

import kpn.api.common.common.Ref
import kpn.server.analyzer.engine.analysis.route.RouteDetailAnalysis

case class NetworkMemberRoute(routeAnalysis: RouteDetailAnalysis, role: Option[String]) {

  def id: Long = routeAnalysis.routeDetail.id

  def toRef: Ref = Ref(id, routeAnalysis.name)
}
