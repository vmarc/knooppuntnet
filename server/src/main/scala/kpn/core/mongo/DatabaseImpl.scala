package kpn.core.mongo

import kpn.api.common.ChangeSetSummary
import kpn.api.common.LocationChangeSetSummary
import kpn.api.common.NodeInfo
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.network.NetworkInfo
import kpn.api.common.route.RouteInfo
import kpn.core.gpx.GpxFile
import kpn.server.analyzer.engine.changes.changes.NetworkElements
import kpn.server.analyzer.engine.changes.changes.RouteElements
import org.mongodb.scala.MongoDatabase

class DatabaseImpl(val database: MongoDatabase) extends Database {

  override def networks: DatabaseCollection[NetworkInfo] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkInfo]("networks"))
  }

  override def networkElements: DatabaseCollection[NetworkElements] = {
    new DatabaseCollectionImpl(database.getCollection[NetworkElements]("network-elements"))
  }

  override def networkGpxs: DatabaseCollection[GpxFile] = {
    new DatabaseCollectionImpl(database.getCollection[GpxFile]("network-gpxs"))
  }

  override def nodes: DatabaseCollection[NodeInfo] = {
    new DatabaseCollectionImpl(database.getCollection[NodeInfo]("nodes"))
  }

  override def routes: DatabaseCollection[RouteInfo] = {
    new DatabaseCollectionImpl(database.getCollection[RouteInfo]("routes"))
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
}
