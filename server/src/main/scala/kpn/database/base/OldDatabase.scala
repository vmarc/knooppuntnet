package kpn.database.base

import kpn.api.base.WithStringId
import kpn.api.common.ChangeSetSummary
import kpn.api.common.PoiState
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.User
import kpn.api.common.poi.Poi
import kpn.core.doc.ChangeSetComment
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NodeDoc
import kpn.core.doc.NodeNetworkRef
import kpn.core.doc.OldRouteDoc
import kpn.core.doc.OrphanNodeDoc
import kpn.core.doc.OrphanRouteDoc
import kpn.core.doc.RouteNetworkRef
import kpn.core.doc.Task
import kpn.database.actions.statistics.StatisticLongValues
import kpn.server.analyzer.engine.changes.data.Blacklist
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRelation
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.domain.MonitorTask
import org.mongodb.scala.MongoCollection

import scala.reflect.ClassTag

trait OldDatabase {

  def getCollection[T: ClassTag](collectionName: String): MongoCollection[T]

  def empty: DatabaseCollection[Any]

  def networks: DatabaseCollection[NetworkDoc]

  def nodes: DatabaseCollection[NodeDoc]

  def orphanNodes: DatabaseCollection[OrphanNodeDoc]

  def oldRoutes: DatabaseCollection[OldRouteDoc]

  def orphanRoutes: DatabaseCollection[OrphanRouteDoc]

  def networkChanges: DatabaseCollection[NetworkChange]

  def routeChanges: DatabaseCollection[RouteChange]

  def nodeChanges: DatabaseCollection[NodeChange]

  def changeSetComments: DatabaseCollection[ChangeSetComment]

  def changes: DatabaseCollection[ChangeSetSummary]

  def nodeNetworkRefs: DatabaseCollection[NodeNetworkRef]

  def routeNetworkRefs: DatabaseCollection[RouteNetworkRef]

  def changeSets: DatabaseCollection[ChangeSetInfo]

  def pois: DatabaseCollection[Poi]

  def poiStates: DatabaseCollection[PoiState]

  def tasks: DatabaseCollection[Task]

  def monitorGroups: DatabaseCollection[MonitorGroup]

  def monitorRoutes: DatabaseCollection[MonitorRoute]

  def monitorRouteReferences: DatabaseCollection[MonitorRouteReference]

  def monitorRouteStates: DatabaseCollection[MonitorRouteState]

  def monitorRouteChanges: DatabaseCollection[MonitorRouteChange]

  def monitorRouteChangeGeometries: DatabaseCollection[MonitorRouteChangeGeometry]

  def monitorRelations: DatabaseCollection[MonitorRelation]

  def monitorTasks: DatabaseCollection[MonitorTask]

  def statistics: DatabaseCollection[StatisticLongValues]

  def status: DatabaseCollection[WithStringId]

  def blacklists: DatabaseCollection[Blacklist]

  def dropDatabase(): Unit

  def users: DatabaseCollection[User]
}
