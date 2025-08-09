package kpn.database.base

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import kpn.api.base.WithStringId
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
import kpn.core.doc.OrphanNodeDoc
import kpn.core.doc.OrphanRouteDoc
import kpn.core.doc.RawNetworkDoc
import kpn.core.doc.RawNodeDoc
import kpn.core.doc.RawRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.doc.RouteNetworkRef
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

class DatabaseImpl(val database: MongoDatabase) extends Database {

  override def getCollection[T: ClassTag](collectionName: String): MongoCollection[T] = {
    database.getCollection(collectionName).asInstanceOf[MongoCollection[T]]
  }

  override def baseNetworks: DatabaseCollection[BaseNetworkDoc] = {
    new DatabaseCollectionImpl(database.getCollection("base-networks", classOf[BaseNetworkDoc]))
  }

  override def networks: DatabaseCollection[NetworkDoc] = {
    new DatabaseCollectionImpl(database.getCollection("networks", classOf[NetworkDoc]))
  }

  override def baseNodes: DatabaseCollection[BaseNodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection("base-nodes", classOf[BaseNodeDoc]))
  }

  override def nodes: DatabaseCollection[NodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection("nodes", classOf[NodeDoc]))
  }

  override def orphanNodes: DatabaseCollection[OrphanNodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection("orphan-nodes", classOf[OrphanNodeDoc]))
  }

  override def routes: DatabaseCollection[RouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection("routes", classOf[RouteDoc]))
  }

  override def baseRoutes: DatabaseCollection[BaseRouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection("base-routes", classOf[BaseRouteDoc]))
  }

  override def routeTiles: DatabaseCollection[RouteTileInfo] = {
    new DatabaseCollectionImpl(database.getCollection("route-tiles", classOf[RouteTileInfo]))
  }

  override def orphanRoutes: DatabaseCollection[OrphanRouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection("orphan-routes", classOf[OrphanRouteDoc]))
  }

  override def networkChanges: DatabaseCollection[NetworkChange] = {
    new DatabaseCollectionImpl(database.getCollection("network-changes", classOf[NetworkChange]))
  }

  override def baseRouteChanges: DatabaseCollection[BaseRouteChange] = {
    new DatabaseCollectionImpl(database.getCollection("base-route-changes", classOf[BaseRouteChange]))
  }

  override def routeChanges: DatabaseCollection[RouteChange] = {
    new DatabaseCollectionImpl(database.getCollection("route-changes", classOf[RouteChange]))
  }

  override def nodeChanges: DatabaseCollection[NodeChange] = {
    new DatabaseCollectionImpl(database.getCollection("node-changes", classOf[NodeChange]))
  }

  override def changeSetComments: DatabaseCollection[ChangeSetComment] = {
    new DatabaseCollectionImpl(database.getCollection("changeset-comments", classOf[ChangeSetComment]))
  }

  override def changes: DatabaseCollection[ChangeSetSummary] = {
    new DatabaseCollectionImpl(database.getCollection("changes", classOf[ChangeSetSummary]))
  }

  override def nodeNetworkRefs: DatabaseCollection[NodeNetworkRef] = {
    new DatabaseCollectionImpl(database.getCollection("node-network-refs", classOf[NodeNetworkRef]))
  }

  override def routeNetworkRefs: DatabaseCollection[RouteNetworkRef] = {
    new DatabaseCollectionImpl(database.getCollection("route-network-refs", classOf[RouteNetworkRef]))
  }

  override def changeSets: DatabaseCollection[ChangeSetInfo] = {
    new DatabaseCollectionImpl(database.getCollection("changesets", classOf[ChangeSetInfo]))
  }

  override def pois: DatabaseCollection[Poi] = {
    new DatabaseCollectionImpl(database.getCollection("pois", classOf[Poi]))
  }

  override def poiStates: DatabaseCollection[PoiState] = {
    new DatabaseCollectionImpl(database.getCollection("poi-states", classOf[PoiState]))
  }

  override def tasks: DatabaseCollection[Task] = {
    new DatabaseCollectionImpl(database.getCollection("tasks", classOf[Task]))
  }

  override def monitorGroups: DatabaseCollection[MonitorGroup] = {
    new DatabaseCollectionImpl(database.getCollection("monitor-groups", classOf[MonitorGroup]))
  }

  override def monitorRoutes: DatabaseCollection[MonitorRoute] = {
    new DatabaseCollectionImpl(database.getCollection("monitor-routes", classOf[MonitorRoute]))
  }

  override def monitorReferences: DatabaseCollection[MonitorReference] = {
    new DatabaseCollectionImpl(database.getCollection("monitor-references", classOf[MonitorReference]))
  }

  override def oldMonitorReferences: DatabaseCollection[OldMonitorReference] = {
    new DatabaseCollectionImpl(database.getCollection("monitor-route-references", classOf[OldMonitorReference]))
  }

  override def monitorReferenceTiles: DatabaseCollection[MonitorReferenceTile] = {
    new DatabaseCollectionImpl(database.getCollection("monitor-reference-tiles", classOf[MonitorReferenceTile]))
  }

  override def monitorStates: DatabaseCollection[MonitorState] = {
    new DatabaseCollectionImpl(database.getCollection("monitor-states", classOf[MonitorState]))
  }

  override def monitorStateTiles: DatabaseCollection[MonitorStateTile] = {
    new DatabaseCollectionImpl(database.getCollection("monitor-state-tiles", classOf[MonitorStateTile]))
  }

  override def monitorRouteChanges: DatabaseCollection[MonitorRouteChange] = {
    new DatabaseCollectionImpl(database.getCollection("monitor-route-changes", classOf[MonitorRouteChange]))
  }

  override def monitorRouteChangeGeometries: DatabaseCollection[MonitorRouteChangeGeometry] = {
    new DatabaseCollectionImpl(database.getCollection("monitor-route-change-geometries", classOf[MonitorRouteChangeGeometry]))
  }

  override def monitorRelations: DatabaseCollection[MonitorRelation] = {
    new DatabaseCollectionImpl(database.getCollection("monitor-relations", classOf[MonitorRelation]))
  }

  override def monitorTasks: DatabaseCollection[MonitorTask] = {
    new DatabaseCollectionImpl(database.getCollection("monitor-tasks", classOf[MonitorTask]))
  }

  override def statistics: DatabaseCollection[StatisticLongValues] = {
    new DatabaseCollectionImpl(database.getCollection("statistics", classOf[StatisticLongValues]))
  }

  override def status: DatabaseCollection[WithStringId] = {
    new DatabaseCollectionImpl(database.getCollection("status", classOf[WithStringId]))
  }

  override def blacklists: DatabaseCollection[Blacklist] = {
    new DatabaseCollectionImpl(database.getCollection("blacklists", classOf[Blacklist]))
  }

  override def dropDatabase(): Unit = {
    database.drop()
  }

  override def users: DatabaseCollection[User] = {
    new DatabaseCollectionImpl(database.getCollection("users", classOf[User]))
  }

  override def transactions: DatabaseCollection[Transaction] = {
    new DatabaseCollectionImpl(database.getCollection("transactions", classOf[Transaction]))
  }

  def rawNodes: DatabaseCollection[RawNodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection("raw-nodes", classOf[RawNodeDoc]))
  }

  def rawRoutes: DatabaseCollection[RawRouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection("raw-routes", classOf[RawRouteDoc]))
  }

  def rawNetworks: DatabaseCollection[RawNetworkDoc] = {
    new DatabaseCollectionImpl(database.getCollection("raw-networks", classOf[RawNetworkDoc]))
  }

  def allRawNodes: DatabaseCollection[RawNodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection("all-raw-nodes", classOf[RawNodeDoc]))
  }

  def allRawRoutes: DatabaseCollection[RawRouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection("all-raw-routes", classOf[RawRouteDoc]))
  }

  def allRawNetworks: DatabaseCollection[RawNetworkDoc] = {
    new DatabaseCollectionImpl(database.getCollection("all-raw-networks", classOf[RawNetworkDoc]))
  }
}
