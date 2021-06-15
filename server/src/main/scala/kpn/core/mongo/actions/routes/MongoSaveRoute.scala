package kpn.core.mongo.actions.routes

import kpn.api.common.route.RouteInfo
import kpn.core.mongo.actions.routes.MongoSaveRoute.log
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.ReplaceOptions

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoSaveRoute {
  private val log = Log(classOf[MongoSaveRoute])
}

class MongoSaveRoute(database: MongoDatabase) {

  def execute(route: RouteInfo): Unit = {
    log.debugElapsed {
      val filter = equal("_id", route._id)
      val collection = database.getCollection[RouteInfo]("routes")
      val future = collection.replaceOne(filter, route, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save route ${route._id}", result)
    }
  }
}
