package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.server.repository.RouteRepositoryImpl

object FindCircularRoutesTool {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: FindCircularRoutesTool host analysisDatabaseName")
      System.exit(-1)
    }
    val host = args(0)
    val analysisDatabaseName = args(1)
    Couch.executeIn(host, analysisDatabaseName) { database =>
      new FindCircularRoutesTool(database).report()
    }
  }
}

class FindCircularRoutesTool(database: Database) {

  private val routeRepository = new RouteRepositoryImpl(null, database, false)

  def report(): Unit = {
    println("Collecting route ids")
    val routeIds = routeRepository.allRouteIds()
    println(s"${routeIds.size} routes")
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if ((index + 1) % 500 == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      routeRepository.routeWithId(routeId) match {
        case None =>
        case Some(routeInfo) =>
          if (routeInfo.active) {
            if (routeInfo.analysis.map.freeNodes.size > 2) {
              if (routeInfo.analysis.map.freeNodes.map(_.name).distinct.size == 1) {
                println("circular route: " + routeId)
              }
            }
          }
      }
    }
    println("done")
  }
}
