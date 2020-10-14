package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.server.repository.RouteRepositoryImpl

object RoutesWithoutTilesTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "attic-analysis") { database =>
      new RoutesWithoutTilesTool(database).report()
    }
  }
}

class RoutesWithoutTilesTool(database: Database) {

  private val routeRepository = new RouteRepositoryImpl(database)

  def report(): Unit = {
    println("Looking for routes without tiles")
    val routeIds = routeRepository.allRouteIds()
    println(s"${routeIds.size} routes")
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if ((index + 1) % 500 == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      routeRepository.routeWithId(routeId) match {
        case None =>
        case Some(route) =>
          if (route.active && !route.summary.isBroken && route.tiles.isEmpty) {
            println(s"* ${route.lastUpdated.yyyymmddhhmmss} $routeId ${route.summary.name} ${route.summary.meters}m ${route.analysis.locationAnalysis.locationNames.mkString(", ")}")
          }
      }
    }
    println("done")
  }
}
