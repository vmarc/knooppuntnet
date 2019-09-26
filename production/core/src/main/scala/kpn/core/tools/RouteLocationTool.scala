package kpn.core.tools

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.views.ViewRow
import kpn.core.engine.analysis.location.LocationsReader
import kpn.core.engine.analysis.location.RouteWayBasedLocatorImpl
import kpn.core.repository.RouteRepositoryImpl
import kpn.shared.route.RouteInfoAnalysis
import spray.json.JsValue

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

  private val locationDefinitions = new LocationsReader().read()
  private val locator = new RouteWayBasedLocatorImpl(locationDefinitions)

  def run(): Unit = {

    val routeIds = readRouteIds()
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

  private def readRouteIds(): Seq[Long] = {
    def toNodeId(row: JsValue): Long = {
      val docId = new ViewRow(row).id.toString
      val routeId = docId.drop("route:".length + 1).dropRight(1)
      routeId.toLong
    }
    val request = """_design/AnalyzerDesign/_view/DocumentView?startkey="route"&endkey="route:a"&reduce=false&stale=ok"""
    database.getRows(request).map(toNodeId).distinct
  }
}
