package kpn.database.base

import kpn.api.base.WithStringId
import kpn.api.common.ChangeSetSummary
import kpn.api.common.PoiState
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.User
import kpn.api.common.poi.Poi
import kpn.core.doc.ChangeSetComment
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkInfoDoc
import kpn.core.doc.NodeDoc
import kpn.core.doc.NodeNetworkRef
import kpn.core.doc.OrphanNodeDoc
import kpn.core.doc.OrphanRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.doc.RouteNetworkRef
import kpn.core.doc.Task
import kpn.database.actions.statistics.StatisticLongValues
import kpn.server.analyzer.engine.changes.data.Blacklist
import kpn.server.analyzer.engine.changes.network.NetworkChange
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRelation
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
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

  override def empty: DatabaseCollection[Any] = {
    new DatabaseCollectionImpl(database.getCollection[Any]("empty"))
  }

  override def networks: DatabaseCollection[NetworkDoc] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkDoc]("networks"))
  }

  override def networkInfos: DatabaseCollection[NetworkInfoDoc] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkInfoDoc]("network-infos"))
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

  override def orphanRoutes: DatabaseCollection[OrphanRouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection[OrphanRouteDoc]("orphan-routes"))
  }

  override def networkChanges: DatabaseCollection[NetworkChange] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkChange]("network-changes"))
  }

  override def networkInfoChanges: DatabaseCollection[NetworkInfoChange] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkInfoChange]("network-info-changes"))
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

  override def monitorRouteReferences: DatabaseCollection[MonitorRouteReference] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorRouteReference]("monitor-route-references"))
  }

  override def monitorRouteStates: DatabaseCollection[MonitorRouteState] = {
    new DatabaseCollectionImpl(database.getCollection[MonitorRouteState]("monitor-route-states"))
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

}
