package kpn.database.base

import kpn.api.base.WithStringId
import kpn.api.common.ChangeSetSummary
import kpn.api.common.Poi
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.statistics.StatisticValues
import kpn.core.doc.ChangeSetComment
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkInfoDoc
import kpn.core.doc.NodeDoc
import kpn.core.doc.NodeNetworkRef
import kpn.core.doc.NodeRouteRef
import kpn.core.doc.OrphanNodeDoc
import kpn.core.doc.OrphanRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.doc.RouteNetworkRef
import kpn.core.doc.Task
import kpn.server.analyzer.engine.changes.data.Blacklist
import kpn.server.analyzer.engine.changes.network.NetworkChange
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.MongoDatabase

import scala.reflect.ClassTag

trait Database {

  @deprecated
  def database: MongoDatabase

  def getCollection[T: ClassTag](collectionName: String): MongoCollection[T]

  def empty: DatabaseCollection[Any]

  def networks: DatabaseCollection[NetworkDoc]

  def networkInfos: DatabaseCollection[NetworkInfoDoc]

  def nodes: DatabaseCollection[NodeDoc]

  def orphanNodes: DatabaseCollection[OrphanNodeDoc]

  def routes: DatabaseCollection[RouteDoc]

  def orphanRoutes: DatabaseCollection[OrphanRouteDoc]

  def networkChanges: DatabaseCollection[NetworkChange]

  def networkInfoChanges: DatabaseCollection[NetworkInfoChange]

  def routeChanges: DatabaseCollection[RouteChange]

  def nodeChanges: DatabaseCollection[NodeChange]

  def changeSetComments: DatabaseCollection[ChangeSetComment]

  def changes: DatabaseCollection[ChangeSetSummary]

  def nodeNetworkRefs: DatabaseCollection[NodeNetworkRef]

  def routeNetworkRefs: DatabaseCollection[RouteNetworkRef]

  def changeSets: DatabaseCollection[ChangeSetInfo]

  def pois: DatabaseCollection[Poi]

  def tasks: DatabaseCollection[Task]

  def monitorGroups: DatabaseCollection[MonitorGroup]

  def monitorRoutes: DatabaseCollection[MonitorRoute]

  def monitorRouteReferences: DatabaseCollection[MonitorRouteReference]

  def monitorRouteStates: DatabaseCollection[MonitorRouteState]

  def monitorRouteChanges: DatabaseCollection[MonitorRouteChange]

  def monitorRouteChangeGeometries: DatabaseCollection[MonitorRouteChangeGeometry]

  def statistics: DatabaseCollection[StatisticValues]

  def status: DatabaseCollection[WithStringId]

  def blacklists: DatabaseCollection[Blacklist]
}
