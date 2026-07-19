package kpn.server.repository

import kpn.api.common.Bounds
import kpn.api.common.Country
import kpn.api.common.common.Reference
import kpn.api.common.route.RouteInfo
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.RouteList
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.NetworkRouteDetail
import kpn.core.doc.ParentRouteData
import kpn.core.doc.RouteDoc
import kpn.core.doc.RouteRelation
import kpn.core.doc.SubRouteData
import kpn.core.doc.SuperSubSegmentInfo
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryBaseRouteIds
import kpn.database.actions.routes.MongoQueryKnownRouteIds
import kpn.database.actions.routes.MongoQueryNetworkRouteDetails
import kpn.database.actions.routes.MongoQueryParentRoutes
import kpn.database.actions.routes.MongoQueryRouteActiveIds
import kpn.database.actions.routes.MongoQueryRouteBounds
import kpn.database.actions.routes.MongoQueryRouteCountry
import kpn.database.actions.routes.MongoQueryRouteDetails
import kpn.database.actions.routes.MongoQueryRouteElementIds
import kpn.database.actions.routes.MongoQueryRouteIds
import kpn.database.actions.routes.MongoQueryRouteInfo
import kpn.database.actions.routes.MongoQueryRouteNetworkReferences
import kpn.database.actions.routes.MongoQueryRouteSearchResults
import kpn.database.actions.routes.MongoQueryRouteSegmentCoordinates
import kpn.database.actions.routes.MongoQueryRouteSegmentCount
import kpn.database.actions.routes.MongoQueryRouteSegments
import kpn.database.actions.routes.MongoQueryRoutes
import kpn.database.actions.routes.MongoQuerySubRelationTree
import kpn.database.actions.routes.MongoQuerySubRouteData
import kpn.database.actions.routes.RouteDetailsData
import kpn.database.base.Database
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import kpn.server.monitor.domain.MonitorSegment
import kpn.server.sync.Transaction
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web", "analysis"))
class RouteRepository(database: Database) {

  private val log = Log(classOf[RouteRepository])

  def allRouteIds(): Seq[Long] = {
    database.routes.ids(log)
  }

  def activeRouteIds(): Seq[Long] = {
    new MongoQueryRouteIds(database).execute(log).sorted
  }

  def activeBaseRouteIds(): Seq[Long] = {
    new MongoQueryBaseRouteIds(database).execute(log).sorted
  }

  def saveRoute(routeDoc: RouteDoc): Unit = {
    database.routes.save(routeDoc, log)
    database.transactions.save(Transaction.routeUpdate(routeDoc._id))
  }

  def bulkSaveRoutes(routeDocs: Seq[RouteDoc]): Unit = {
    database.routes.bulkSave(routeDocs, log)
    val transactions = routeDocs.map { routeDoc =>
      val transaction = Transaction.routeUpdate(routeDoc._id)
      database.transactions.save(transaction)
    }
  }

  def findRouteById(routeId: Long): Option[RouteDoc] = {
    database.routes.findById(routeId, log)
  }

  def routeInfo(routeId: Long): Option[RouteInfo] = {
    new MongoQueryRouteInfo(database).execute(routeId, log)
  }

  def routeSegmentCount(routeId: Long): Option[Long] = {
    new MongoQueryRouteSegmentCount(database).execute(routeId, log)
  }

  def networkReferences(routeId: Long): Seq[Reference] = {
    new MongoQueryRouteNetworkReferences(database).execute(routeId, log)
  }

  def routeCountry(routeId: Long): Option[Country] = {
    new MongoQueryRouteCountry(database).execute(routeId)
  }

  def explore(query: ConditionGroup): RouteList = {
    val routeIds = new MongoQueryRoutes(database).execute(query)
    new MongoQueryRouteSearchResults(database).execute(routeIds)
  }

  def activeRouteElementIds(): Seq[ReferencedElementIds] = {
    new MongoQueryRouteElementIds(database).execute()
  }

  def saveBaseRoute(baseRoute: BaseRouteDoc): Unit = {
    database.baseRoutes.save(baseRoute, log)
  }

  def bulkSaveBaseRoutes(baseRoutes: Seq[BaseRouteDoc]): Unit = {
    database.baseRoutes.bulkSave(baseRoutes, log)
  }

  def findBaseRouteById(routeId: Long): Option[BaseRouteDoc] = {
    database.baseRoutes.findById(routeId, log)
  }

  def filterKnownBaseRoutes(routeIds: Set[Long]): Set[Long] = {
    new MongoQueryKnownRouteIds(database).execute(routeIds.toSeq, log).toSet
  }

  def bounds(routeIds: Seq[Long]): Option[Bounds] = {
    new MongoQueryRouteBounds(database).execute(routeIds, log)
  }

  def segments(routeIds: Seq[Long]): Seq[SuperSubSegmentInfo] = {
    new MongoQueryRouteSegments(database).execute(routeIds, log)
  }

  def routeActiveIds(routeIds: Seq[Long]): Seq[Long] = {
    new MongoQueryRouteActiveIds(database).execute(routeIds, log)
  }

  def subRouteData(routeId: Long): Option[SubRouteData] = {
    new MongoQuerySubRouteData(database).execute(routeId)
  }

  def parentRoutes(routeId: Long): Seq[ParentRouteData] = {
    new MongoQueryParentRoutes(database).execute(routeId)
  }

  def networkRouteDetails(routeIds: Seq[Long]): Seq[NetworkRouteDetail] = {
    new MongoQueryNetworkRouteDetails(database).execute(routeIds)
  }

  def subRelationTree(routeId: Long): Option[RouteRelation] = {
    new MongoQuerySubRelationTree(database).execute(routeId)
  }

  def segmentCoordinates(routeIds: Seq[Long]): Seq[MonitorSegment] = {
    new MongoQueryRouteSegmentCoordinates(database).execute(routeIds)
  }

  def routeDetails(routeId: Long): Option[RouteDetailsData] = {
    new MongoQueryRouteDetails(database).execute(routeId)
  }
}
