package kpn.core.mongo.changes

import kpn.api.common.changes.details.RouteChange
import kpn.core.mongo.changes.MongoSaveRouteChange.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoSaveRouteChange {
  private val log = Log(classOf[MongoSaveRouteChange])
}

class MongoSaveRouteChange(database: MongoDatabase) {

  def execute(routeChange: RouteChange): Unit = {
    log.debugElapsed {
      val filter = equal("_id", routeChange._id)
      val collection = database.getCollection[RouteChange]("route-changes")
      val future = collection.replaceOne(filter, routeChange, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save ${routeChange._id}", result)
    }
  }
}
