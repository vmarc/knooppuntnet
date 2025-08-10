package kpn.core.tools.monitor.support

import kpn.api.custom.Relation
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.RouteSymbol
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import org.bson.types.ObjectId

object MonitorMigrateSymbolsTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor") { database =>
      new MonitorMigrateSymbolsTool(database).migrate()
    }
  }
}

class MonitorMigrateSymbolsTool(database: Database) {

  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
  private val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)

  def migrate(): Unit = {
    val routeIds = database.monitorRoutes.objectIds()
    val routeIdsSize = routeIds.size
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      println(s"${index + 1}/$routeIdsSize")
      migrateRoute(routeId)
    }
  }

  private def migrateRoute(routeId: ObjectId): Unit = {
    database.monitorRoutes.findByObjectId(routeId).foreach { monitorRoute =>
      monitorRoute.relationId.foreach { relationId =>
        monitorRouteRelationRepository.loadTopLevel(None, relationId).foreach { relation =>
          migrateSymbol(monitorRoute, relation)
        }
      }
    }
  }

  private def migrateSymbol(monitorRoute: MonitorRoute, relation: Relation): Unit = {
    RouteSymbol.from(relation) match {
      case None =>
        if (monitorRoute.symbol.isDefined) {
          val updatedMonitorRoute = monitorRoute.copy(symbol = None)
          database.monitorRoutes.save(updatedMonitorRoute)
        }
      case Some(symbol) =>
        if (!monitorRoute.symbol.contains(symbol)) {
          val updatedMonitorRoute = monitorRoute.copy(symbol = Some(symbol))
          database.monitorRoutes.save(updatedMonitorRoute)
        }
    }
  }
}
