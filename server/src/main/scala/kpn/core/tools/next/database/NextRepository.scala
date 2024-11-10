package kpn.core.tools.next.database

import kpn.core.tools.next.domain.NextRouteRelation

trait NextRepository {

  def nextRouteRelation(routeId: Long): Option[NextRouteRelation]

  def allRouteIds(): Seq[Long]
}
