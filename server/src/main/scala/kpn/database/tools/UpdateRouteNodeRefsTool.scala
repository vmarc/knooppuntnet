package kpn.database.tools

import kpn.database.base.Database
import kpn.core.doc.RouteDoc
import kpn.database.util.Mongo
import org.mongodb.scala.model.Filters.exists

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
    val future = database.routes.native.find[RouteDoc](exists("nodeRefs", exists = false)).toFuture()
    val routeDocs = Await.result(future, Duration(30, TimeUnit.SECONDS))
    routeDocs.zipWithIndex.foreach { case (routeDoc, index) =>
      println(s"${index + 1}/${routeDocs.size}")
      val migrated = routeDoc.copy(nodeRefs = routeDoc.analysis.map.nodeIds)
      database.routes.save(migrated)
    }
  }
}
