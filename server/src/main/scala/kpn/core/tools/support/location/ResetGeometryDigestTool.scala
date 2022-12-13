package kpn.core.tools.support.location

import kpn.database.base.Database
import kpn.database.util.Mongo

object ResetGeometryDigestTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new ResetGeometryDigestTool(database).run()
    }
    println("Done")
  }
}

class ResetGeometryDigestTool(database: Database) {

  def run(): Unit = {
    val routes = new RoutesWithoutLocationQuery(database).execute()
    routes.zipWithIndex.foreach { case (route, index) =>
      println(s"${index + 1}/${routes.size} ${route._id} ${route.name}")
      database.routes.findById(route._id) match {
        case None =>
        case Some(route) =>
          database.routes.save(
            route.copy(
              analysis = route.analysis.copy(geometryDigest = "")
            )
          )
      }
    }
  }
}
