package kpn.core.mongo.tools

import kpn.api.common.route.RouteInfo
import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo
import org.mongodb.scala.bson.BsonDocument

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object UpdateRouteNodeRefsTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new UpdateRouteNodeRefsTool(database).update()
    }
  }
}

class UpdateRouteNodeRefsTool(database: Database) {

  def update(): Unit = {
    val future = database.routes.native.find[RouteInfo](BsonDocument("""{"nodeRefs": {"$exists": false}}""")).toFuture()
    val routeInfos = Await.result(future, Duration(30, TimeUnit.SECONDS))
    routeInfos.zipWithIndex.foreach { case (routeInfo, index) =>
      println(s"${index + 1}/${routeInfos.size}")
      val migrated = routeInfo.copy(nodeRefs = routeInfo.analysis.map.nodeIds)
      database.routes.save(migrated)
    }
  }
}
