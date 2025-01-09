package kpn.server.repository

import kpn.api.common.Bounds
import kpn.api.common.RouteType
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.ParentRouteData
import kpn.core.doc.SubRouteData
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryKnownRouteIds
import kpn.database.actions.routes.MongoQueryParentRoutes
import kpn.database.actions.routes.MongoQueryRouteBounds
import kpn.database.actions.routes.MongoQueryRouteElementIds
import kpn.database.actions.routes.MongoQueryRouteTileInfo
import kpn.database.actions.routes.MongoQuerySubRouteData
import kpn.database.base.Database
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import org.springframework.stereotype.Component

@Component
class BaseRouteRepositoryImpl(database: Database) extends BaseRouteRepository {

  private val log = Log(classOf[BaseRouteRepositoryImpl])

  override def activeRouteElementIds(): Seq[ReferencedElementIds] = {
    new MongoQueryRouteElementIds(database).execute()
  }

  override def save(baseRoute: BaseRouteDoc): Unit = {
    database.baseRoutes.save(baseRoute, log)
  }

  override def bulkSave(baseRoutes: Seq[BaseRouteDoc]): Unit = {
    database.baseRoutes.bulkSave(baseRoutes, log)
  }

  override def findById(routeId: Long): Option[BaseRouteDoc] = {
    database.baseRoutes.findById(routeId, log)
  }

  override def filterKnown(routeIds: Set[Long]): Set[Long] = {
    new MongoQueryKnownRouteIds(database).execute(routeIds.toSeq, log).toSet
  }

  override def routeTileInfosByrouteType(routeType: RouteType, nodeNetwork: Boolean): Seq[RouteTileInfo] = {
    new MongoQueryRouteTileInfo(database).findByrouteType(routeType, nodeNetwork)
  }

  override def bounds(routeIds: Seq[Long]): Option[Bounds] = {
    new MongoQueryRouteBounds(database).execute(routeIds, log)
  }

  override def subRouteData(routeId: Long): Option[SubRouteData] = {
    new MongoQuerySubRouteData(database).execute(routeId)
  }

  override def parentRoutes(routeId: Long): Seq[ParentRouteData] = {
    new MongoQueryParentRoutes(database).execute(routeId)
  }
}
