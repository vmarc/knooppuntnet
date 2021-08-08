package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

object FindNetworksThatBecameRoutesTool {

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: FindNetworksThatBecameRoutesTool host analysisDatabaseName")
      System.exit(-1)
    }
    val host = args(0)
    val analysisDatabaseName = args(1)
    Couch.executeIn(host, analysisDatabaseName) { database =>
      new FindNetworksThatBecameRoutesTool(database).report()
    }
  }
}

class FindNetworksThatBecameRoutesTool(database: Database) {

  private val networkRepository = new NetworkRepositoryImpl(null, database, false)
  private val routeRepository = new RouteRepositoryImpl(null, database, false)

  def report(): Unit = {
    println("Collecting network ids")
    val networkIds = networkRepository.allNetworkIds().toSet
    println("Collecting route ids")
    val routeIds = routeRepository.allRouteIds().toSet
    println(s"Found ${networkIds.size} networks, ${routeIds.size} routes")

    val commonIds = networkIds.intersect(routeIds).filter(bothActive)
    if (commonIds.isEmpty) {
      println("OK, no networks and routes with same id")
    }
    else {
      println(s"Networks and routes with same id:")
      commonIds.foreach(println)
    }
    println("done")
  }

  private def bothActive(id: Long): Boolean = {
    networkRepository.findById(id) match {
      case None => false
      case Some(network) =>
        if (network.active) {
          routeRepository.routeWithId(id) match {
            case None => false
            case Some(route) => route.active
          }
        }
        else {
          false
        }
    }
  }
}
