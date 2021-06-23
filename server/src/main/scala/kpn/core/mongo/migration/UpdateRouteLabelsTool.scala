package kpn.core.mongo.migration

import kpn.api.common.route.RouteInfo
import kpn.core.mongo.Database
import kpn.core.mongo.DatabaseCollectionImpl
import kpn.core.mongo.migration.UpdateRouteLabelsTool.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLabelsBuilder

object UpdateRouteLabelsTool {

  private val log = Log(classOf[UpdateRouteLabelsTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new UpdateRouteLabelsTool(database).migrate()
    }
    log.info("Done")
  }
}

class UpdateRouteLabelsTool(database: Database) {

  val newRoutes = new DatabaseCollectionImpl(database.getCollection[RouteInfo]("routes-new"))

  def migrate(): Unit = {
    val allRouteIds = database.routes.ids(log)
    val batchSize = 50
    allRouteIds.sliding(batchSize, batchSize).zipWithIndex.foreach { case (routeIds, index) =>
      log.info(s"${index * batchSize}/${allRouteIds.size}")
      migrateRoutes(routeIds)
    }
  }

  private def migrateRoutes(routeIds: Seq[Long]): Unit = {
    val routeInfos = database.routes.findByIds(routeIds)
    val migratedRouteInfos = routeInfos.map { routeInfo =>
      val labels = new RouteLabelsBuilder().build(routeInfo)
      routeInfo.copy(
        labels = labels
      )
    }
    newRoutes.insertMany(migratedRouteInfos)
  }
}
