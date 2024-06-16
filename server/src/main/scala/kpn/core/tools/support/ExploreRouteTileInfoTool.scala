package kpn.core.tools.support

import kpn.database.util.Mongo
import kpn.server.repository.RouteRepositoryImpl

object ExploreRouteTileInfoTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      val routeRepository = new RouteRepositoryImpl(database)
      val route1 = routeRepository.routeTileInfosById(17144685L)
      val route2 = routeRepository.routeTileInfosById(17147794L)
      val route3 = routeRepository.routeTileInfosById(17144687L)
      println(route1)
      println("---")
      println(route2)
      println("---")
      println(route3)
    }
  }
}
