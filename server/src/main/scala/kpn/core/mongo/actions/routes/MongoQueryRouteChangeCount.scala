package kpn.core.mongo.actions.routes

import kpn.core.mongo.Database
import kpn.core.mongo.actions.routes.MongoQueryRouteChangeCount.log
import kpn.core.util.Log
import org.mongodb.scala.model.Filters.equal

object MongoQueryRouteChangeCount {
  private val log = Log(classOf[MongoQueryRouteChangeCount])
}

class MongoQueryRouteChangeCount(database: Database) {

  def execute(routeId: Long): Long = {
    log.debugElapsed {
      val filter = equal("key.elementId", routeId)
      val count = database.routeChanges.countDocuments(filter)
      (s"route $routeId change count: $count", count)
    }
  }
}
