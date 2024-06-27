package kpn.core.tools.next.support

import kpn.database.util.Mongo

// make a list of all routes where route.summary.tags != route.tags
object OldRouteTagsTool {
  def main(args: Array[String]): Unit = {
    val oldDatabase = Mongo.oldDatabase(Mongo.client, "kpn-next")
    val allRouteIds = oldDatabase.oldRoutes.ids()
    val investigateRouteIds = allRouteIds.zipWithIndex.flatMap { case (routeId, index) =>
      if (((index + 1) % 100) == 0) {
        println(s"${index + 1}/${allRouteIds.size}")
      }
      oldDatabase.oldRoutes.findById(routeId) match {
        case None => None
        case Some(oldRouteDoc) =>
          if (oldRouteDoc.summary.tags != oldRouteDoc.summary.tags) {
            println(s"investigate  route $routeId")
            Some(routeId)
          }
          else {
            None
          }
      }
    }
    investigateRouteIds.foreach(println)
  }
}
