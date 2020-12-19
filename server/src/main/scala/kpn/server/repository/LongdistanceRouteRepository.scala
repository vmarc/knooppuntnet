package kpn.server.repository

import kpn.api.common.monitor.LongdistanceRoute
import kpn.api.common.monitor.LongdistanceRouteChange

trait LongdistanceRouteRepository {

  def save(routeInfo: LongdistanceRoute): Unit

  def routeWithId(routeId: Long): Option[LongdistanceRoute]

  def all(): Seq[LongdistanceRoute]

  def saveChange(change: LongdistanceRouteChange): Unit

  def changes(): Seq[LongdistanceRouteChange]

  def change(routeId: Long, changeSetId: Long): Option[LongdistanceRouteChange]
}
