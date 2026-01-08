package kpn.core.tools.support

import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.repository.RouteRepositoryImpl

object FindRoutesWithoutNodeNetworkTagTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new FindRoutesWithoutNodeNetworkTagTool(database).report()
    }
  }
}

class FindRoutesWithoutNodeNetworkTagTool(database: Database) {

  private val routeRepository = new RouteRepositoryImpl(database)

  def report(): Unit = {
    println("Collecting route ids")
    val routeIds = routeRepository.allRouteIds()
    println(s"${routeIds.size} routes")
    val nonNetworkRouteIds = routeIds.zipWithIndex.flatMap { case (routeId, index) =>
      if ((index + 1) % 500 == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      routeRepository.findById(routeId).flatMap { doc =>
        if (doc.isActive && !doc.tags.has("network:type", "node_network")) {
          println(s"found $routeId")
          Some(routeId)
        }
        else {
          None
        }
      }
    }
    println("results:")
    nonNetworkRouteIds.foreach(println)
    println("done")
  }
}
