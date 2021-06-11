package kpn.core.mongo.changes

import kpn.api.common.changes.details.RouteChange
import kpn.core.database.doc.RouteChangeDoc
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
      val routeId = routeChange.id
      val id = s"change:${routeChange.key.changeSetId}:${routeChange.key.replicationNumber}:route:$routeId"
      val doc = RouteChangeDoc(id, routeChange)
      val filter = equal("_id", id)
      val collection = database.getCollection[RouteChangeDoc]("route-change")
      val future = collection.replaceOne(filter, doc, ReplaceOptions().upsert(true)).toFuture()
      val result = Await.result(future, Duration(30, TimeUnit.SECONDS))
      (s"save $id", result)
    }
  }
}
