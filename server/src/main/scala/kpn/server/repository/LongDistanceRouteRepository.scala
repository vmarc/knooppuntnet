package kpn.server.repository

import kpn.api.common.longdistance.LongDistanceRoute
import kpn.api.common.longdistance.LongDistanceRouteChange

trait LongDistanceRouteRepository {

  def save(routeInfo: LongDistanceRoute): Unit

  def routeWithId(routeId: Long): Option[LongDistanceRoute]

  def all(): Seq[LongDistanceRoute]

  def saveChange(change: LongDistanceRouteChange): Unit

  def changes(): Seq[LongDistanceRouteChange]

  def change(changeSetId: Long, routeId: Long): Option[LongDistanceRouteChange]
}
