package kpn.core.mongo

import kpn.api.common.ChangeSetSummary
import kpn.api.common.LocationChangeSetSummary
import kpn.api.common.Poi
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.network.NetworkInfo
import kpn.api.common.route.RouteInfo
import kpn.core.gpx.GpxFile
import kpn.core.planner.graph.GraphEdge
import kpn.server.analyzer.engine.changes.changes.NetworkElements
import kpn.server.analyzer.engine.changes.changes.RouteElements
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

  override def networks: DatabaseCollection[NetworkInfo] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkInfo]("networks"))
  }

  override def networkElements: DatabaseCollection[NetworkElements] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkElements]("network-elements"))
  }

  override def networkGpxs: DatabaseCollection[GpxFile] = {
    new DatabaseCollectionImpl(database.getCollection[GpxFile]("network-gpxs"))
  }

  override def nodes: DatabaseCollection[NodeDoc] = {
    new DatabaseCollectionImpl(database.getCollection[NodeDoc]("nodes"))
  }

  override def routes: DatabaseCollection[RouteInfo] = {
    new DatabaseCollectionImpl(database.getCollection[RouteInfo]("routes"))
  }

  override def routeEdges: DatabaseCollection[GraphEdge] = {
    new DatabaseCollectionImpl(database.getCollection[GraphEdge]("route-edges"))
  }

  override def routeElements: DatabaseCollection[RouteElements] = {
    new DatabaseCollectionImpl(database.getCollection[RouteElements]("route-elements"))
  }

  override def networkChanges: DatabaseCollection[NetworkChange] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkChange]("network-changes"))
  }

  override def routeChanges: DatabaseCollection[RouteChange] = {
    new DatabaseCollectionImpl(database.getCollection[RouteChange]("route-changes"))
  }

  override def nodeChanges: DatabaseCollection[NodeChange] = {
    new DatabaseCollectionImpl(database.getCollection[NodeChange]("node-changes"))
  }

  override def changeSetSummaries: DatabaseCollection[ChangeSetSummary] = {
    new DatabaseCollectionImpl(database.getCollection[ChangeSetSummary]("changeset-summaries"))
  }

  override def locationChangeSetSummaries: DatabaseCollection[LocationChangeSetSummary] = {
    new DatabaseCollectionImpl(database.getCollection[LocationChangeSetSummary]("change-location-summaries"))
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
}
