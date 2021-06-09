package kpn.core.mongo.changes

import kpn.core.mongo.changes.MongoQueryRouteChangeCount.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryRouteChangeCount {
  private val log = Log(classOf[MongoQueryRouteChangeCount])
}

class MongoQueryRouteChangeCount(database: MongoDatabase) {

  def execute(routeId: Long): Long = {
    log.debugElapsed {
      val filter = equal("routeChange.key.elementId", routeId)
      val collection = database.getCollection("route-changes")
      val future = collection.countDocuments(filter).toFuture()
      val count = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"route $routeId change count: $count", count)
    }
  }
}
