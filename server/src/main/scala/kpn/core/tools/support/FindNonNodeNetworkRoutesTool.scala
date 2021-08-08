package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.server.repository.RouteRepositoryImpl

object FindNonNodeNetworkRoutesTool {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: FindNonNodeNetworkRoutesTool host analysisDatabaseName")
      System.exit(-1)
    }
    val host = args(0)
    val analysisDatabaseName = args(1)
    Couch.executeIn(host, analysisDatabaseName) { database =>
      new FindNonNodeNetworkRoutesTool(database).report()
    }
  }
}

class FindNonNodeNetworkRoutesTool(database: Database) {

  private val routeRepository = new RouteRepositoryImpl(null, database, false)

  def report(): Unit = {
    println("Collecting route ids")
    val routeIds = routeRepository.allRouteIds()
    println(s"${routeIds.size} routes")
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if ((index + 1) % 500 == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      routeRepository.findById(routeId) match {
        case None =>
        case Some(routeInfo) =>
          if (routeInfo.active) {
            if (!routeInfo.tags.has("network:type", "node_network")) {
              println("route=" + routeId)
            }
          }
      }
    }
    println("done")
  }
}
