package kpn.core.mongo.actions.routes

import kpn.api.common.route.RouteInfo
import kpn.core.database.doc.RouteDoc
import kpn.core.mongo.Database
import kpn.core.mongo.actions.routes.MongoQueryRoute.log
import kpn.core.util.Log
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryRoute {
  private val log = Log(classOf[MongoQueryRoute])
}

class MongoQueryRoute(database: Database) {

  def execute(routeId: Long): Option[RouteInfo] = {
    log.debugElapsed {
      val collection = database.database.getCollection[RouteDoc]("routes")
      val future = collection.find[RouteDoc](equal("_id", routeId)).headOption()
      val route = Await.result(future, Duration(30, TimeUnit.SECONDS)).map(_.route)
      (s"route $routeId", route)
    }
  }
}
