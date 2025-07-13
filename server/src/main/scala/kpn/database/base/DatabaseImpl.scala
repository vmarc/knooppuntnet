package kpn.database.base

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
import org.mongodb.scala.*
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

class DatabaseImpl(val database: MongoDatabase) extends Database {

  override def getCollection[T: ClassTag](collectionName: String): MongoCollection[T] = {
    database.getCollection[T](collectionName)
  }

  override def baseNetworks: DatabaseCollection[BaseNetworkDoc] = {
    new DatabaseCollectionImpl(database.getCollection[BaseNetworkDoc]("base-networks"))
  }

  override def networks: DatabaseCollection[NetworkDoc] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkDoc]("networks"))
  }

  override def baseNodes: DatabaseCollection[BaseNodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection[BaseNodeDoc]("base-nodes"))
  }

  override def nodes: DatabaseCollection[NodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection[NodeDoc]("nodes"))
  }

  override def orphanNodes: DatabaseCollection[OrphanNodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection[OrphanNodeDoc]("orphan-nodes"))
  }

  override def routes: DatabaseCollection[RouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection[RouteDoc]("routes"))
  }

  override def baseRoutes: DatabaseCollection[BaseRouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection[BaseRouteDoc]("base-routes"))
  }

  override def routeTiles: DatabaseCollection[RouteTileInfo] = {
    new DatabaseCollectionImpl(database.getCollection[RouteTileInfo]("route-tiles"))
  }

  override def orphanRoutes: DatabaseCollection[OrphanRouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection[OrphanRouteDoc]("orphan-routes"))
  }

  override def networkChanges: DatabaseCollection[NetworkChange] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkChange]("network-changes"))
  }

  override def baseRouteChanges: DatabaseCollection[BaseRouteChange] = {
    new DatabaseCollectionImpl(database.getCollection[BaseRouteChange]("base-route-changes"))
  }

  override def routeChanges: DatabaseCollection[RouteChange] = {
    new DatabaseCollectionImpl(database.getCollection[RouteChange]("route-changes"))
  }

  override def nodeChanges: DatabaseCollection[NodeChange] = {
    new DatabaseCollectionImpl(database.getCollection[NodeChange]("node-changes"))
  }

  override def changeSetComments: DatabaseCollection[ChangeSetComment] = {
    new DatabaseCollectionImpl(database.getCollection[ChangeSetComment]("changeset-comments"))
  }

  override def changes: DatabaseCollection[ChangeSetSummary] = {
    new DatabaseCollectionImpl(database.getCollection[ChangeSetSummary]("changes"))
  }

  override def nodeNetworkRefs: DatabaseCollection[NodeNetworkRef] = {
    new DatabaseCollectionImpl(database.getCollection[NodeNetworkRef]("node-network-refs"))
  }

  override def routeNetworkRefs: DatabaseCollection[RouteNetworkRef] = {
    new DatabaseCollectionImpl(database.getCollection[RouteNetworkRef]("route-network-refs"))
  }

  override def changeSets: DatabaseCollection[ChangeSetInfo] = {
    new DatabaseCollectionImpl(database.getCollection[ChangeSetInfo]("changesets"))
  }

  override def pois: DatabaseCollection[Poi] = {
    new DatabaseCollectionImpl(database.getCollection[Poi]("pois"))
  }

  override def poiStates: DatabaseCollection[PoiState] = {
    new DatabaseCollectionImpl(database.getCollection[PoiState]("poi-states"))
  }

  override def tasks: DatabaseCollection[Task] = {
    new DatabaseCollectionImpl(database.getCollection[Task]("tasks"))
  }

  override def monitorGroups: DatabaseCollection[MonitorGroup] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorGroup]("monitor-groups"))
  }

  override def monitorRoutes: DatabaseCollection[MonitorRoute] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorRoute]("monitor-routes"))
  }

  override def monitorReferences: DatabaseCollection[MonitorReference] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorReference]("monitor-references"))
  }

  override def oldMonitorReferences: DatabaseCollection[OldMonitorReference] = {
    new DatabaseCollectionImpl(database.getCollection[OldMonitorReference]("monitor-route-references"))
  }

  override def monitorReferenceTiles: DatabaseCollection[MonitorReferenceTile] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorReferenceTile]("monitor-reference-tiles"))
  }

  override def monitorStates: DatabaseCollection[MonitorState] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorState]("monitor-states"))
  }

  override def monitorStateTiles: DatabaseCollection[MonitorStateTile] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorStateTile]("monitor-state-tiles"))
  }

  override def monitorRouteChanges: DatabaseCollection[MonitorRouteChange] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorRouteChange]("monitor-route-changes"))
  }

  override def monitorRouteChangeGeometries: DatabaseCollection[MonitorRouteChangeGeometry] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorRouteChangeGeometry]("monitor-route-change-geometries"))
  }

  override def monitorRelations: DatabaseCollection[MonitorRelation] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorRelation]("monitor-relations"))
  }

  override def monitorTasks: DatabaseCollection[MonitorTask] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorTask]("monitor-tasks"))
  }

  override def statistics: DatabaseCollection[StatisticLongValues] = {
    new DatabaseCollectionImpl(database.getCollection[StatisticLongValues]("statistics"))
  }

  override def status: DatabaseCollection[WithStringId] = {
    new DatabaseCollectionImpl(database.getCollection[WithStringId]("status"))
  }

  override def blacklists: DatabaseCollection[Blacklist] = {
    new DatabaseCollectionImpl(database.getCollection[Blacklist]("blacklists"))
  }

  override def dropDatabase(): Unit = {
    Await.result(database.drop().toFuture(), Duration(2, TimeUnit.SECONDS))
  }

  override def users: DatabaseCollection[User] = {
    new DatabaseCollectionImpl(database.getCollection[User]("users"))
  }

  override def transactions: DatabaseCollection[Transaction] = {
    new DatabaseCollectionImpl(database.getCollection[Transaction]("transactions"))
  }

  def rawNodes: DatabaseCollection[RawNodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection[RawNodeDoc]("raw-nodes"))
  }

  def rawRoutes: DatabaseCollection[RawRouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection[RawRouteDoc]("raw-routes"))
  }

  def rawNetworks: DatabaseCollection[RawNetworkDoc] = {
    new DatabaseCollectionImpl(database.getCollection[RawNetworkDoc]("raw-networks"))
  }

  def allRawNodes: DatabaseCollection[RawNodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection[RawNodeDoc]("all-raw-nodes"))
  }

  def allRawRoutes: DatabaseCollection[RawRouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection[RawRouteDoc]("all-raw-routes"))
  }

  def allRawNetworks: DatabaseCollection[RawNetworkDoc] = {
    new DatabaseCollectionImpl(database.getCollection[RawNetworkDoc]("all-raw-networks"))
  }
}
