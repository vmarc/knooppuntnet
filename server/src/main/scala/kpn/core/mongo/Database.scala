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
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.MongoDatabase

import scala.reflect.ClassTag

trait Database {

  @deprecated
  def database: MongoDatabase

  def getCollection[T: ClassTag](collectionName: String): MongoCollection[T]

  def empty: DatabaseCollection[Any]

  def networks: DatabaseCollection[NetworkDoc]

  def networkInfos: DatabaseCollection[NetworkInfoDoc]

  def networkShapes: DatabaseCollection[NetworkShapeDoc]

  def oldNetworks: DatabaseCollection[NetworkInfo]

  def networkGpxs: DatabaseCollection[GpxFile]

  def nodes: DatabaseCollection[NodeDoc]

  def orphanNodes: DatabaseCollection[OrphanNodeDoc]

  def routes: DatabaseCollection[RouteInfo]

  def orphanRoutes: DatabaseCollection[OrphanRouteDoc]

  def routeEdges: DatabaseCollection[GraphEdge]

  def networkChanges: DatabaseCollection[NetworkChange]

  def routeChanges: DatabaseCollection[RouteChange]

  def nodeChanges: DatabaseCollection[NodeChange]

  def changeSetComments: DatabaseCollection[ChangeSetComment]

  def changeSetSummaries: DatabaseCollection[ChangeSetSummary]

  def locationChangeSetSummaries: DatabaseCollection[LocationChangeSetSummary]

  def nodeNetworkRefs: DatabaseCollection[NodeNetworkRef]

  def routeNetworkRefs: DatabaseCollection[RouteNetworkRef]

  def nodeRouteRefs: DatabaseCollection[NodeRouteRef]

  def changeSets: DatabaseCollection[ChangeSetInfo]

  def pois: DatabaseCollection[Poi]

  def tasks: DatabaseCollection[Task]

  def monitorGroups: DatabaseCollection[MonitorGroup]

  def monitorRoutes: DatabaseCollection[MonitorRoute]

  def monitorRouteReferences: DatabaseCollection[MonitorRouteReference]

  def monitorRouteStates: DatabaseCollection[MonitorRouteState]

  def monitorRouteChanges: DatabaseCollection[MonitorRouteChange]

  def monitorRouteChangeGeometries: DatabaseCollection[MonitorRouteChangeGeometry]

  def statistics: DatabaseCollection[StatisticValues]

}
