package kpn.core.tools.support

import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.repository.RouteRepositoryImpl

/*
  Prepares an overview of all values for tag "colour" that can be found
  in route relations.
 */
object RouteColoursTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new RouteColoursTool(database).report()
    }
  }
}

class RouteColoursTool(database: Database) {

  private val knownColours = Seq(
    "black",
    "blue",
    "brown",
    "gray",
    "green",
    "grey",
    "orange",
    "purple",
    "red",
    "violet",
    "white",
    "yellow"
  )

  private val routeRepository = new RouteRepositoryImpl(database)

  def report(): Unit = {
    println("Collecting route ids")
    val routeIds = routeRepository.allRouteIds()
    val routeIdsSize = routeIds.size
    println(s"${routeIds.size} routes")
    val colours = routeIds.zipWithIndex.flatMap { case (routeId, index) =>
      if ((index + 1) % 500 == 0) {
        println(s"${index + 1}/$routeIdsSize")
      }
      val colours = routeRepository.findRouteById(routeId).toSeq.flatMap(_.summary.tagValues("colour")).distinct.sorted
      if (colours.exists(colour => !knownColours.contains(colour))) {
        println(s"$routeId ${colours.mkString("|")}")
      }
      colours
    }
    val colourFrequencyMap = colours.groupBy(identity).view.mapValues(_.size)
    colourFrequencyMap.keys.foreach { colour =>
      println(s"$colour ${colourFrequencyMap.get(colour)}")
    }
    println("done")
  }
}
