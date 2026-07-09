package kpn.core.tools.support.location

import kpn.database.base.Database
import kpn.database.util.Mongo

object RoutesWithoutLocationReportTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new RoutesWithoutLocationReportTool(database).run()
    }
    println("Done")
  }
}

class RoutesWithoutLocationReportTool(database: Database) {

  def run(): Unit = {
    val routes = new RoutesWithoutLocationQuery(database).execute()
    routes.zipWithIndex.foreach { case (route, index) =>
      println(s"${index + 1} ${route._id} ${route.name}")
    }
  }
}
