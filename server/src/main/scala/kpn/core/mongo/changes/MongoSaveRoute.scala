package kpn.core.mongo.changes

import kpn.api.common.route.RouteInfo
import kpn.core.database.doc.RouteDoc
import kpn.core.mongo.changes.MongoSaveRoute.log
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
      val routeId = route.id
      val id = s"route:$routeId"
      val doc = RouteDoc(id, route)
      val filter = equal("_id", id)
      val collection = database.getCollection[RouteDoc]("routes")
      val future = collection.replaceOne(filter, doc, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save route $routeId", result)
    }
  }
}
