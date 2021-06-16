package kpn.core.mongo.actions.routes

import kpn.core.mongo.Database
import kpn.core.mongo.actions.routes.MongoQueryRouteChangeCount.log
import kpn.core.util.Log
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryRouteChangeCount {
  private val log = Log(classOf[MongoQueryRouteChangeCount])
}

class MongoQueryRouteChangeCount(database: Database) {

  def execute(routeId: Long): Long = {
    log.debugElapsed {
      val filter = equal("key.elementId", routeId)
      val collection = database.database.getCollection("route-changes")
      val future = collection.countDocuments(filter).toFuture()
      val count = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"route $routeId change count: $count", count)
    }
  }
}
