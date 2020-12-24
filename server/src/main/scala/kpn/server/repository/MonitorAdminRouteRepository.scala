package kpn.server.repository

import kpn.api.common.changes.details.ChangeKeyI
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState

trait MonitorAdminRouteRepository {

  def allRouteIds: Seq[Long]

  def saveRoute(route: MonitorRoute): Unit

  def saveRouteState(routeState: MonitorRouteState): Unit

  def saveRouteReference(routeReference: MonitorRouteReference): Unit

  def saveRouteChange(routeChange: MonitorRouteChange): Unit

  def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit

  def route(routeId: Long): Option[MonitorRoute]

  def routeState(routeId: Long): Option[MonitorRouteState]

  def routeReference(routeId: Long, key: String): Option[MonitorRouteReference]

  def routeChange(changeKey: ChangeKeyI): Option[MonitorRouteChange]

  def routeChangeGeometry(changeKey: ChangeKeyI): Option[MonitorRouteChangeGeometry]
}
