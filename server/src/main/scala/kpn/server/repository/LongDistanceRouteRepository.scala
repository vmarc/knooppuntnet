package kpn.server.repository

import kpn.api.common.longdistance.LongDistanceRoute

trait LongDistanceRouteRepository {

  def save(routeInfo: LongDistanceRoute): Unit

  def routeWithId(routeId: Long): Option[LongDistanceRoute]

  def all(): Seq[LongDistanceRoute]

}
