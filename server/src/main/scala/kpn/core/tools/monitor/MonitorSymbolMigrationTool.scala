package kpn.core.tools.monitor

import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.RouteSymbol
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.route.update.MonitorRouteRelationRepository

case class RelationId(relationId: Long)

object MonitorSymbolMigrationTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor") { database =>
      new MonitorSymbolMigrationTool(database).migrate()
    }
  }
}

class MonitorSymbolMigrationTool(database: Database) {

  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
  private val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)

  def migrate(): Unit = {
    val routeIds = database.monitorRoutes.objectIds()
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      println(s"${index + 1}/${routeIds.size}")
      database.monitorRoutes.findByObjectId(routeId).foreach { monitorRoute =>
        monitorRoute.relationId.foreach { relationId =>
          monitorRouteRelationRepository.loadTopLevel(None, relationId).foreach { relation =>
            RouteSymbol.from(relation.tags) match {
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
      }
    }
  }
}
