package kpn.core.mongo.migration

import kpn.core.database.doc.RouteDoc
import kpn.core.mongo.NodeRouteRef
import kpn.core.mongo.util.Mongo
import org.mongodb.scala.Observer
import org.mongodb.scala.model.Filters.equal

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object InsertNodeRouteRefs {
  def main(args: Array[String]): Unit = {
    println("Insert node route refs")
    var count = 0
    val mongoClient = Mongo.client
    val database = Mongo.database(mongoClient, "tryout")
    val routesCollection = database.getCollection[RouteDoc]("routes")
    val nodeRouteRefs = database.getCollection[NodeRouteRef]("nodeRouteRefs")
    routesCollection.find[RouteDoc](equal("route.active", true)).subscribe(new Observer[RouteDoc]() {
      override def onNext(routeDoc: RouteDoc): Unit = {
        count = count + 1
        val refs = routeDoc.route.analysis.map.nodeIds.map { nodeId =>
          val summary = routeDoc.route.summary
          val id = s"${summary.networkType.name}:$nodeId:${summary.id}"
          NodeRouteRef(
            id,
            summary.networkType,
            nodeId,
            summary.id,
            summary.name
          )
        }
        if (refs.nonEmpty) {
          val insertManyResultFuture = nodeRouteRefs.insertMany(refs).toFuture()
          val insertManyResult = Await.result(insertManyResultFuture, Duration(30, TimeUnit.SECONDS))
          println(s"$count route ${routeDoc.route.summary.id} insert acknowledged: ${insertManyResult.wasAcknowledged}, ${refs.size} refs")
        }
        else {
          println(s"$count route ${routeDoc.route.summary.id} refs empty")
        }
      }

      override def onError(e: Throwable): Unit = {
        println("Error " + e.getMessage)
      }

      override def onComplete(): Unit = {
        mongoClient.close()
        println("Done")
      }
    })
    println("Sleep")
    Thread.sleep(1000 * 60 * 60 * 2)
    println("Exit")
  }
}
