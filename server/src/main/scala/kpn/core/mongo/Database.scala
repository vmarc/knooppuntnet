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

trait Database {

  @deprecated
  def database: MongoDatabase

  def networks: DatabaseCollection[NetworkInfo]

  def networkElements: DatabaseCollection[NetworkElements]

  def networkGpxs: DatabaseCollection[GpxFile]

  def nodes: DatabaseCollection[NodeInfo]

  def routes: DatabaseCollection[RouteInfo]

  def routeElements: DatabaseCollection[RouteElements]

  def networkChanges: DatabaseCollection[NetworkChange]

  def routeChanges: DatabaseCollection[RouteChange]

  def nodeChanges: DatabaseCollection[NodeChange]

  def changeSetSummaries: DatabaseCollection[ChangeSetSummary]

  def locationChangeSetSummaries: DatabaseCollection[LocationChangeSetSummary]

  def nodeNetworkRefs: DatabaseCollection[NodeNetworkRef]

  def routeNetworkRefs: DatabaseCollection[RouteNetworkRef]

  def nodeRouteRefs: DatabaseCollection[NodeRouteRef]
}
