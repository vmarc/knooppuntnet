package kpn.server.repository

import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState

trait MonitorRouteRepository {

  def allRouteIds: Seq[Long]

  def saveRoute(route: MonitorRoute): Unit

  def saveRouteState(routeState: MonitorRouteState): Unit

  def saveRouteReference(routeReference: MonitorRouteReference): Unit

  def saveRouteChange(routeChange: MonitorRouteChange): Unit

  def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit

  def route(routeId: Long): Option[MonitorRoute]

  def routeState(routeId: Long): Option[MonitorRouteState]

  def routeReference(routeId: Long, key: String): Option[MonitorRouteReference]

  def routeChange(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChange]

  def routeChangeGeometry(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangeGeometry]

  def changesCount(parameters: MonitorChangesParameters): Long

  def changes(parameters: MonitorChangesParameters): Seq[MonitorRouteChange]

  def groupChangesCount(groupName: String, parameters: MonitorChangesParameters): Long

  def groupChanges(groupName: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange]

  def routeChangesCount(routeId: Long, parameters: MonitorChangesParameters): Long

  def routeChanges(routeId: Long, parameters: MonitorChangesParameters): Seq[MonitorRouteChange]

  def routes(): Seq[MonitorRoute]

  def routeChange(changeKey: ChangeKey): Option[MonitorRouteChange]

  def routeChangeGeometry(changeKey: ChangeKey): Option[MonitorRouteChangeGeometry]

  def routeReferenceKey(routeId: Long): Option[String]

}
