package kpn.server.repository

import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState

trait MonitorRouteRepository {

  def route(routeId: Long): Option[MonitorRoute]

  def routeState(routeId: Long): Option[MonitorRouteState]

  def routeReference(routeId: Long, key: String): Option[MonitorRouteReference]

  def routeChange(routeId: Long, changeSetId: Long, replicationId: Long): Option[MonitorRouteChange]

  def routeChangeGeometry(routeId: Long, changeSetId: Long, replicationId: Long): Option[MonitorRouteChangeGeometry]

}
