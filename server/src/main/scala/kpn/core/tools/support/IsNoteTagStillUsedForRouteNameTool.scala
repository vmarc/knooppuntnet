package kpn.core.tools.support

import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo

object IsNoteTagStillUsedForRouteNameTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test-3") { database =>
      new IsNoteTagStillUsedForRouteNameTool(database).investigate()
    }
  }
}

class IsNoteTagStillUsedForRouteNameTool(database: Database) {

  def investigate(): Unit = {
    database.routes.ids().foreach { routeId =>
      database.routes.findById(routeId) foreach { route =>
        if (route.tags.has("note")) {
          println(s"route=$routeId, tags: ${route.tags}")
        }
      }
    }
  }
}
