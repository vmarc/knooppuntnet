package kpn.database.base

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import kpn.api.common.ChangeSetSummary
import kpn.api.common.PoiState
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.User
import kpn.api.common.poi.Poi
import kpn.api.id.WithStringId
import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.BaseNodeDoc
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.ChangeSetComment
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NodeDoc
import kpn.core.doc.NodeNetworkRef
import kpn.core.doc.RawNetworkDoc
import kpn.core.doc.RawNodeDoc
import kpn.core.doc.RawRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.doc.Task
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

class Database(val database: MongoDatabase) {

  def getCollection[T: ClassTag](collectionName: String): DatabaseCollection[T] = {
    new DatabaseCollection(database.getCollection(collectionName).asInstanceOf[MongoCollection[T]])
  }

  def baseNetworks: DatabaseCollection[BaseNetworkDoc] = {
    new DatabaseCollection(database.getCollection("base-networks", classOf[BaseNetworkDoc]))
  }

  def networks: DatabaseCollection[NetworkDoc] = {
    new DatabaseCollection(database.getCollection("networks", classOf[NetworkDoc]))
  }

  def baseNodes: DatabaseCollection[BaseNodeDoc] = {
    new DatabaseCollection(database.getCollection("base-nodes", classOf[BaseNodeDoc]))
  }

  def nodes: DatabaseCollection[NodeDoc] = {
    new DatabaseCollection(database.getCollection("nodes", classOf[NodeDoc]))
  }

  def routes: DatabaseCollection[RouteDoc] = {
    new DatabaseCollection(database.getCollection("routes", classOf[RouteDoc]))
  }

  def baseRoutes: DatabaseCollection[BaseRouteDoc] = {
    new DatabaseCollection(database.getCollection("base-routes", classOf[BaseRouteDoc]))
  }

  def routeTiles: DatabaseCollection[RouteTileInfo] = {
    new DatabaseCollection(database.getCollection("route-tiles", classOf[RouteTileInfo]))
  }

  def networkChanges: DatabaseCollection[NetworkChange] = {
    new DatabaseCollection(database.getCollection("network-changes", classOf[NetworkChange]))
  }

  def baseRouteChanges: DatabaseCollection[BaseRouteChange] = {
    new DatabaseCollection(database.getCollection("base-route-changes", classOf[BaseRouteChange]))
  }

  def routeChanges: DatabaseCollection[RouteChange] = {
    new DatabaseCollection(database.getCollection("route-changes", classOf[RouteChange]))
  }

  def nodeChanges: DatabaseCollection[NodeChange] = {
    new DatabaseCollection(database.getCollection("node-changes", classOf[NodeChange]))
  }

  def changeSetComments: DatabaseCollection[ChangeSetComment] = {
    new DatabaseCollection(database.getCollection("changeset-comments", classOf[ChangeSetComment]))
  }

  def changes: DatabaseCollection[ChangeSetSummary] = {
    new DatabaseCollection(database.getCollection("changes", classOf[ChangeSetSummary]))
  }

  def nodeNetworkRefs: DatabaseCollection[NodeNetworkRef] = {
    new DatabaseCollection(database.getCollection("node-network-refs", classOf[NodeNetworkRef]))
  }

  def changeSets: DatabaseCollection[ChangeSetInfo] = {
    new DatabaseCollection(database.getCollection("changesets", classOf[ChangeSetInfo]))
  }

  def pois: DatabaseCollection[Poi] = {
    new DatabaseCollection(database.getCollection("pois", classOf[Poi]))
  }

  def poiStates: DatabaseCollection[PoiState] = {
    new DatabaseCollection(database.getCollection("poi-states", classOf[PoiState]))
  }

  def tasks: DatabaseCollection[Task] = {
    new DatabaseCollection(database.getCollection("tasks", classOf[Task]))
  }

  def monitorGroups: DatabaseCollection[MonitorGroup] = {
    new DatabaseCollection(database.getCollection("monitor-groups", classOf[MonitorGroup]))
  }

  def monitorRoutes: DatabaseCollection[MonitorRoute] = {
    new DatabaseCollection(database.getCollection("monitor-routes", classOf[MonitorRoute]))
  }

  def monitorReferences: DatabaseCollection[MonitorReference] = {
    new DatabaseCollection(database.getCollection("monitor-references", classOf[MonitorReference]))
  }

  def oldMonitorReferences: DatabaseCollection[OldMonitorReference] = {
    new DatabaseCollection(database.getCollection("monitor-route-references", classOf[OldMonitorReference]))
  }

  def monitorReferenceTiles: DatabaseCollection[MonitorReferenceTile] = {
    new DatabaseCollection(database.getCollection("monitor-reference-tiles", classOf[MonitorReferenceTile]))
  }

  def monitorStates: DatabaseCollection[MonitorState] = {
    new DatabaseCollection(database.getCollection("monitor-states", classOf[MonitorState]))
  }

  def monitorStateTiles: DatabaseCollection[MonitorStateTile] = {
    new DatabaseCollection(database.getCollection("monitor-state-tiles", classOf[MonitorStateTile]))
  }

  def monitorRouteChanges: DatabaseCollection[MonitorRouteChange] = {
    new DatabaseCollection(database.getCollection("monitor-route-changes", classOf[MonitorRouteChange]))
  }

  def monitorRouteChangeGeometries: DatabaseCollection[MonitorRouteChangeGeometry] = {
    new DatabaseCollection(database.getCollection("monitor-route-change-geometries", classOf[MonitorRouteChangeGeometry]))
  }

  def monitorRelations: DatabaseCollection[MonitorRelation] = {
    new DatabaseCollection(database.getCollection("monitor-relations", classOf[MonitorRelation]))
  }

  def monitorTasks: DatabaseCollection[MonitorTask] = {
    new DatabaseCollection(database.getCollection("monitor-tasks", classOf[MonitorTask]))
  }

  def statistics: DatabaseCollection[StatisticLongValues] = {
    new DatabaseCollection(database.getCollection("statistics", classOf[StatisticLongValues]))
  }

  def status: DatabaseCollection[WithStringId] = {
    new DatabaseCollection(database.getCollection("status", classOf[WithStringId]))
  }

  def blacklists: DatabaseCollection[Blacklist] = {
    new DatabaseCollection(database.getCollection("blacklists", classOf[Blacklist]))
  }

  def dropDatabase(): Unit = {
    database.drop()
  }

  def users: DatabaseCollection[User] = {
    new DatabaseCollection(database.getCollection("users", classOf[User]))
  }

  def transactions: DatabaseCollection[Transaction] = {
    new DatabaseCollection(database.getCollection("transactions", classOf[Transaction]))
  }

  def rawNodes: DatabaseCollection[RawNodeDoc] = {
    new DatabaseCollection(database.getCollection("raw-nodes", classOf[RawNodeDoc]))
  }

  def rawRoutes: DatabaseCollection[RawRouteDoc] = {
    new DatabaseCollection(database.getCollection("raw-routes", classOf[RawRouteDoc]))
  }

  def rawNetworks: DatabaseCollection[RawNetworkDoc] = {
    new DatabaseCollection(database.getCollection("raw-networks", classOf[RawNetworkDoc]))
  }

  def allRawNodes: DatabaseCollection[RawNodeDoc] = {
    new DatabaseCollection(database.getCollection("all-raw-nodes", classOf[RawNodeDoc]))
  }

  def allRawRoutes: DatabaseCollection[RawRouteDoc] = {
    new DatabaseCollection(database.getCollection("all-raw-routes", classOf[RawRouteDoc]))
  }

  def allRawNetworks: DatabaseCollection[RawNetworkDoc] = {
    new DatabaseCollection(database.getCollection("all-raw-networks", classOf[RawNetworkDoc]))
  }
}
