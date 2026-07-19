package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteChangeCount.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal

object MongoQueryRouteChangeCount {
  private val log = Log(classOf[MongoQueryRouteChangeCount])
}

class MongoQueryRouteChangeCount(database: Database) {

  def execute(routeId: Long): Long = {
    log.debugElapsed {
      val filter = equal("key.elementId", routeId)
      val count = database.routeChanges.countFilteredDocuments(filter)
      (s"route $routeId change count: $count", count)
    }
  }
}
