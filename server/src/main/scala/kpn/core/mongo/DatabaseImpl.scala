package kpn.core.mongo

import kpn.api.base.WithStringId
import kpn.api.common.ChangeSetSummary
import kpn.api.common.Poi
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.route.RouteInfo
import kpn.api.common.statistics.StatisticValues
import kpn.core.gpx.GpxFile
import kpn.core.mongo.doc.NetworkDoc
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.mongo.doc.NetworkShapeDoc
import kpn.core.mongo.doc.NodeDoc
import kpn.core.mongo.doc.OrphanNodeDoc
import kpn.core.mongo.doc.OrphanRouteDoc
import kpn.core.mongo.migration.ChangeSetComment
import kpn.core.planner.graph.GraphEdge
import kpn.server.analyzer.engine.changes.network.NetworkChange
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.MongoDatabase

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

  override def networkShapes: DatabaseCollection[NetworkShapeDoc] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkShapeDoc]("network-shapes"))
  }

  override def networkGpxs: DatabaseCollection[GpxFile] = {
    new DatabaseCollectionImpl(database.getCollection[GpxFile]("network-gpxs"))
  }

  override def nodes: DatabaseCollection[NodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection[NodeDoc]("nodes"))
  }

  override def orphanNodes: DatabaseCollection[OrphanNodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection[OrphanNodeDoc]("orphan-nodes"))
  }

  override def routes: DatabaseCollection[RouteInfo] = {
    new DatabaseCollectionImpl(database.getCollection[RouteInfo]("routes"))
  }

  override def orphanRoutes: DatabaseCollection[OrphanRouteDoc] = {
    new DatabaseCollectionImpl(database.getCollection[OrphanRouteDoc]("orphan-routes"))
  }

  override def routeEdges: DatabaseCollection[GraphEdge] = {
    new DatabaseCollectionImpl(database.getCollection[GraphEdge]("route-edges"))
  }

  override def networkChanges: DatabaseCollection[NetworkChange] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkChange]("new-network-changes"))
  }

  override def networkInfoChanges: DatabaseCollection[NetworkInfoChange] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkInfoChange]("network-changes"))
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

  override def nodeRouteRefs: DatabaseCollection[NodeRouteRef] = {
    new DatabaseCollectionImpl(database.getCollection[NodeRouteRef]("node-route-refs"))
  }

  override def changeSets: DatabaseCollection[ChangeSetInfo] = {
    new DatabaseCollectionImpl(database.getCollection[ChangeSetInfo]("changesets"))
  }

  override def pois: DatabaseCollection[Poi] = {
    new DatabaseCollectionImpl(database.getCollection[Poi]("pois"))
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

  override def statistics: DatabaseCollection[StatisticValues] = {
    new DatabaseCollectionImpl(database.getCollection[StatisticValues]("statistics"))
  }

  override def status: DatabaseCollection[WithStringId] = {
    new DatabaseCollectionImpl(database.getCollection[WithStringId]("status"))
  }

}
