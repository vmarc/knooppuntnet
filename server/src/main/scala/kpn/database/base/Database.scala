package kpn.database.base

import kpn.api.common.ChangeSetSummary
import kpn.api.common.PoiState
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.User
import kpn.api.common.poi.Poi
import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.BaseNodeDoc
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.ChangeSetComment
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NodeDoc
import kpn.core.doc.NodeNetworkRef
import kpn.core.doc.OrphanRouteDoc
import kpn.core.doc.RawNetworkDoc
import kpn.core.doc.RawNodeDoc
import kpn.core.doc.RawRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.doc.RouteNetworkRef
import kpn.core.doc.Task
import kpn.core.doc.WithStringId
import kpn.database.actions.statistics.StatisticLongValues
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.changes.data.Blacklist
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorReferenceTile
import kpn.server.monitor.domain.MonitorRelation
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.domain.MonitorStateTile
import kpn.server.monitor.domain.MonitorTask
import kpn.server.monitor.domain.OldMonitorReference
import kpn.server.sync.Transaction

import scala.reflect.ClassTag

trait Database {

  def getCollection[T: ClassTag](collectionName: String): DatabaseCollection[T]

  def baseNetworks: DatabaseCollection[BaseNetworkDoc]

  def networks: DatabaseCollection[NetworkDoc]

  def baseNodes: DatabaseCollection[BaseNodeDoc]

  def nodes: DatabaseCollection[NodeDoc]

  def routes: DatabaseCollection[RouteDoc]

  def baseRoutes: DatabaseCollection[BaseRouteDoc]

  def routeTiles: DatabaseCollection[RouteTileInfo]

  def orphanRoutes: DatabaseCollection[OrphanRouteDoc]

  def networkChanges: DatabaseCollection[NetworkChange]

  def baseRouteChanges: DatabaseCollection[BaseRouteChange]

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

  def monitorReferences: DatabaseCollection[MonitorReference]

  def oldMonitorReferences: DatabaseCollection[OldMonitorReference]

  def monitorReferenceTiles: DatabaseCollection[MonitorReferenceTile]

  def monitorStates: DatabaseCollection[MonitorState]

  def monitorStateTiles: DatabaseCollection[MonitorStateTile]

  def monitorRouteChanges: DatabaseCollection[MonitorRouteChange]

  def monitorRouteChangeGeometries: DatabaseCollection[MonitorRouteChangeGeometry]

  def monitorRelations: DatabaseCollection[MonitorRelation]

  def monitorTasks: DatabaseCollection[MonitorTask]

  def statistics: DatabaseCollection[StatisticLongValues]

  def status: DatabaseCollection[WithStringId]

  def blacklists: DatabaseCollection[Blacklist]

  def dropDatabase(): Unit

  def users: DatabaseCollection[User]

  def transactions: DatabaseCollection[Transaction]

  def rawNodes: DatabaseCollection[RawNodeDoc]

  def rawRoutes: DatabaseCollection[RawRouteDoc]

  def rawNetworks: DatabaseCollection[RawNetworkDoc]

  def allRawNodes: DatabaseCollection[RawNodeDoc]

  def allRawRoutes: DatabaseCollection[RawRouteDoc]

  def allRawNetworks: DatabaseCollection[RawNetworkDoc]
}
