package kpn.core.mongo.migration

import kpn.api.common.route.RouteInfo
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.mongo.NodeRouteRef
import kpn.core.mongo.migration.MigrateRoutesTool.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.repository.RouteRepositoryImpl
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MigrateRoutesTool {

  private val log = Log(classOf[MigrateRoutesTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { mongoDatabase =>
      Couch.executeIn("kpn-database", "analysis") { couchDatabase =>
        new MigrateRoutesTool(couchDatabase, mongoDatabase).migrate()
      }
    }
    log.info("Done")
  }
}

class MigrateRoutesTool(couchDatabase: Database, mongoDatabase: MongoDatabase) {

  private val routeRepository = new RouteRepositoryImpl(couchDatabase, false, null)
  private val routesCollection = mongoDatabase.getCollection[RouteInfo]("routes")
  private val nodeRouteRefsCollection = mongoDatabase.getCollection[NodeRouteRef]("node-route-refs")

  def migrate(): Unit = {
    val batchSize = 25
    val allRouteIds = findAllRouteIds()
    allRouteIds.sliding(batchSize, batchSize).zipWithIndex.foreach { case (routeIds, index) =>
      log.info(s"${index * batchSize}/${allRouteIds.size}")
      migrateRoutes(routeIds)
    }
  }

  private def findAllRouteIds(): Seq[Long] = {
    log.info("Collecting routeIds")
    routeRepository.allRouteIds()
  }

  private def migrateRoutes(routeIds: Seq[Long]): Unit = {
    val routeInfos = routeRepository.routesWithIds(routeIds).map(routeInfo => routeInfo.copy(_id = routeInfo.id))
    val insertManyResultFuture = routesCollection.insertMany(routeInfos).toFuture()
    Await.result(insertManyResultFuture, Duration(3, TimeUnit.MINUTES))
    routeInfos.foreach(migrateNodeRouteRefs)
  }

  private def migrateNodeRouteRefs(routeInfo: RouteInfo): Unit = {
    val refs = routeInfo.analysis.map.nodeIds.map { nodeId =>
      val summary = routeInfo.summary
      val _id = s"$nodeId:${summary.id}"
      NodeRouteRef(
        _id,
        nodeId,
        summary.id,
        summary.networkType,
        summary.name
      )
    }
    if (refs.nonEmpty) {
      val insertManyResultFuture = nodeRouteRefsCollection.insertMany(refs).toFuture()
      Await.result(insertManyResultFuture, Duration(30, TimeUnit.SECONDS))
    }
  }
}
