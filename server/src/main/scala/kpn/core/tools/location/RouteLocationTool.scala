package kpn.core.tools.location

import kpn.api.common.route.RouteInfoAnalysis
import kpn.core.database.Database
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.analysis.location.RouteWayBasedLocatorImpl
import kpn.server.repository.RouteRepositoryImpl

/*
  Performs route location analysis for all routes in the database.

  This is for testing purposes during development only.  This code can/should be
  removed once all location analysis logic is fully in place.
 */
object RouteLocationTool {

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      println("Usage: RouteLocationTool host masterDbName")
      System.exit(-1)
    }
    val host = args(0)
    val masterDbName = args(1)

    Couch.executeIn(host, masterDbName) { database =>
      println("Start")
      new RouteLocationTool(database).run()
      println("Done")
    }
  }

}

class RouteLocationTool(database: Database) {

  private val locator = {
    val configuration = new LocationConfigurationReader().read()
    new RouteWayBasedLocatorImpl(configuration)
  }

  def run(): Unit = {

    val routeIds = DocumentView.allRouteIds(database)
    println(s"Updating ${routeIds.size} route definitions")
    val repo = new RouteRepositoryImpl(database)
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if ((index + 1) % 100 == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      repo.routeWithId(routeId, Couch.uiTimeout).foreach { routeInfo =>
        routeInfo.analysis match {
          case Some(analysis: RouteInfoAnalysis) =>
            analysis.locationAnalysis match {
              case None =>
                locator.locate(routeInfo) match {
                  case Some(locationAnalysis) =>
                    val newAnalysis = analysis.copy(locationAnalysis = Some(locationAnalysis))
                    repo.save(routeInfo.copy(analysis = Some(newAnalysis)))
                  case None =>
                    println(s"Could not determine location of route $routeId")
                }
              case _ =>
            }
          case _ =>
        }
      }
    }
  }
}
