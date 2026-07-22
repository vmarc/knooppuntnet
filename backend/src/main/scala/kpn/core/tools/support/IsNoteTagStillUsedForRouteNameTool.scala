package kpn.core.tools.support

import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.route.base.analyzers.NoteTagAnalyzer

object IsNoteTagStillUsedForRouteNameTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new IsNoteTagStillUsedForRouteNameTool(database).investigate()
    }
  }
}

class IsNoteTagStillUsedForRouteNameTool(database: Database) {

  def investigate(): Unit = {
    val routeIds = database.routes.ids()
    val routeIdsSize = routeIds.size
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if ((index % 1000) == 0) {
        println(s"$index/$routeIdsSize")
      }
      database.routes.findById(routeId) foreach { route =>
        if (route.active) {
          if (!route.hasTag("ref") && !route.hasTag("name")) {
            route.tagValue("note") match {
              case None =>
              case Some(note) =>
                if (!NoteTagAnalyzer.isDeprecatedNoteTag(note)) {
                  println(s"route=$routeId: [${route.tagValue("note").get}]")
                  println(s"    tags: ${route.tags}")
                }
            }
          }
        }
      }
    }
  }
}
