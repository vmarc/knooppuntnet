package kpn.core.mongo.changes

import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MongoQueryRouteChangeCount(database: MongoDatabase) {

  private val log = Log(classOf[MongoQueryRouteChangeCount])

  def execute(routeId: Long): Long = {
    log.debugElapsed {
      val future = database.getCollection("route-changes").countDocuments(equal("routeChange.key.elementId", routeId)).toFuture()
      val count = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"route $routeId change count: $count", count)
    }
  }
}
