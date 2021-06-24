package kpn.core.mongo.migration

import kpn.core.mongo.Database
import kpn.core.mongo.RouteEdges
import kpn.core.mongo.migration.UpdateRouteEdgesTool.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteGraphEdgesBuilder

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object UpdateRouteEdgesTool {
  private val log = Log(classOf[UpdateRouteEdgesTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new UpdateRouteEdgesTool(database).update()
    }
  }
}

class UpdateRouteEdgesTool(database: Database) {

  def update(): Unit = {
    val routeEdges = database.getCollection[Any]("route-edges")
    val allRouteIds = database.routes.ids(log)
    val batchSize = 200
    allRouteIds.sliding(batchSize, batchSize).zipWithIndex.foreach { case (routeIds, index) =>
      log.info(s"${index * batchSize}/${allRouteIds.size}")
      val edges = database.routes.findByIds(routeIds, log).map { routeInfo =>
        val edges = new RouteGraphEdgesBuilder().build(routeInfo.id, routeInfo.analysis.map)
        RouteEdges(routeInfo._id, routeInfo.summary.networkType, edges)
      }
      val future = routeEdges.insertMany(edges).toFuture()
      Await.result(future, Duration(1, TimeUnit.MINUTES))
    }
  }
}
