package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.server.repository.RouteRepositoryImpl

/*
  Prepares an overview of all values for tag "colour" that can be found
  in route relations.
 */
object RouteColoursTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "analysis") { database =>
      new RouteColoursTool(database).report()
    }
  }
}

class RouteColoursTool(database: Database) {

  private val routeRepository = new RouteRepositoryImpl(database)

  def report(): Unit = {
    println("Collecting route ids")
    val routeIds = routeRepository.allRouteIds()
    println(s"${routeIds.size} routes")
    val colours = routeIds.zipWithIndex.flatMap { case (routeId, index) =>
      if ((index + 1) % 500 == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      routeRepository.routeWithId(routeId).toSeq.flatMap(_.tags("colour"))
    }
    val colourFrequencyMap = colours.groupBy(identity).view.mapValues(_.size)
    colourFrequencyMap.keys.foreach { colour =>
      println(s"$colour ${colourFrequencyMap.get(colour)}")
    }
    println("done")
  }
}
