package kpn.server.repository

import kpn.api.common.monitor.MonitorRoute
import kpn.api.common.monitor.MonitorRouteChange

trait MonitorRouteRepository {

  def save(routeInfo: MonitorRoute): Unit

  def routeWithId(routeId: Long): Option[MonitorRoute]

  def all(): Seq[MonitorRoute]

  def saveChange(change: MonitorRouteChange): Unit

  def changes(): Seq[MonitorRouteChange]

  def change(routeId: Long, changeSetId: Long): Option[MonitorRouteChange]
}
