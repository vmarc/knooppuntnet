package kpn.core.mongo

import kpn.api.common.ChangeSetSummary
import kpn.api.common.LocationChangeSetSummary
import kpn.api.common.NodeInfo
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.network.NetworkInfo
import kpn.api.common.route.RouteInfo
import org.mongodb.scala.MongoDatabase

class DatabaseImpl(val database: MongoDatabase) extends Database {

  override def networks: DatabaseCollection[NetworkInfo] = {
    new DatabaseCollection(database.getCollection[NetworkInfo]("networks"))
  }

  override def nodes: DatabaseCollection[NodeInfo] = {
    new DatabaseCollection(database.getCollection[NodeInfo]("nodes"))
  }

  override def routes: DatabaseCollection[RouteInfo] = {
    new DatabaseCollection(database.getCollection[RouteInfo]("routes"))
  }

  override def networkChanges: DatabaseCollection[NetworkChange] = {
    new DatabaseCollection(database.getCollection[NetworkChange]("network-changes"))
  }

  override def routeChanges: DatabaseCollection[RouteChange] = {
    new DatabaseCollection(database.getCollection[RouteChange]("route-changes"))
  }

  override def nodeChanges: DatabaseCollection[NodeChange] = {
    new DatabaseCollection(database.getCollection[NodeChange]("node-changes"))
  }

  override def changeSetSummaries: DatabaseCollection[ChangeSetSummary] = {
    new DatabaseCollection(database.getCollection[ChangeSetSummary]("changeset-summaries"))
  }

  override def locationChangeSetSummaries: DatabaseCollection[LocationChangeSetSummary] = {
    new DatabaseCollection(database.getCollection[LocationChangeSetSummary]("change-location-summaries"))
  }

  override def nodeNetworkRefs: DatabaseCollection[NodeNetworkRef] = {
    new DatabaseCollection(database.getCollection[NodeNetworkRef]("node-network-refs"))
  }

  override def routeNetworkRefs: DatabaseCollection[RouteNetworkRef] = {
    new DatabaseCollection(database.getCollection[RouteNetworkRef]("route-network-refs"))
  }

  override def nodeRouteRefs: DatabaseCollection[NodeRouteRef] = {
    new DatabaseCollection(database.getCollection[NodeRouteRef]("node-route-refs"))
  }
}
