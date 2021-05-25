package kpn.core.mongo

import kpn.core.database.Database
import kpn.core.database.doc.RouteDoc
import kpn.core.db.couch.Couch
import kpn.server.repository.RouteRepositoryImpl

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object InsertRoutes {

  def main(args: Array[String]): Unit = {
    println("Insert routes")
    Couch.executeIn("kpn-database", "analysis") { couchDatabase =>
      migrate(couchDatabase)
      println("Done")
    }
  }

  private def migrate(couchDatabase: Database): Unit = {
    val mongoClient = Mongo.client
    val database = Mongo.database(mongoClient, "tryout")
    val routesCollection = database.getCollection[RouteDoc]("routes")
    val repo = new RouteRepositoryImpl(couchDatabase)
    val allRouteIds = repo.allRouteIds()
    println(s"Migrating ${allRouteIds.size} routes")
    val batchSize = 25
    allRouteIds.sliding(batchSize, batchSize).zipWithIndex.foreach { case (routeIds, index) =>
      println(s"${index * batchSize}/${allRouteIds.size}")
      val routeInfos = repo.routesWithIds(routeIds)
      val routeDocs = routeInfos.map(n => RouteDoc(s"route:${n.id}", n))
      val insertManyResultFuture = routesCollection.insertMany(routeDocs).toFuture()
      val insertManyResult = Await.result(insertManyResultFuture, Duration(3, TimeUnit.MINUTES))
      println(s"Insert acknowledged: ${insertManyResult.wasAcknowledged}, ${routeIds.size} routes")
    }
    mongoClient.close()
  }
}
