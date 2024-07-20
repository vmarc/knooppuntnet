package kpn.core.analysis

import kpn.api.common.common.Ref
import kpn.api.common.diff.RouteData

case class NetworkMemberRoute(data: RouteData, role: Option[String]) {

  def id: Long = data.relationId

  def toRef: Ref = Ref(data.relationId, data.name)
}
