package kpn.core.mongo.migration

import kpn.api.common.route.RouteInfo
import kpn.core.db.couch.Couch
import kpn.core.mongo.Database
import kpn.core.mongo.NodeRouteRef
import kpn.core.mongo.migration.MigrateRoutesTool.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.repository.RouteRepositoryImpl

object MigrateRoutesTool {

  private val log = Log(classOf[MigrateRoutesTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      Couch.executeIn("kpn-database", "analysis") { couchDatabase =>
        // new MigrateRoutesTool(couchDatabase, database).migrate()
        new MigrateRoutesTool(couchDatabase, database).migrateRouteElements()
      }
    }
    log.info("Done")
  }
}

class MigrateRoutesTool(couchDatabase: kpn.core.database.Database, database: Database) {

  private val routeRepository = new RouteRepositoryImpl(null, couchDatabase, false)

  def migrate(): Unit = {
    val batchSize = 25
    val allRouteIds = findAllRouteIds()
    allRouteIds.sliding(batchSize, batchSize).zipWithIndex.foreach { case (routeIds, index) =>
      log.info(s"${index * batchSize}/${allRouteIds.size}")
      migrateRoutes(routeIds)
    }
  }

  def migrateRouteElements(): Unit = {
    val batchSize = 25
    val allRouteIds = findAllRouteIds()
    allRouteIds.sliding(batchSize, batchSize).zipWithIndex.foreach { case (routeIds, index) =>
      log.info(s"${index * batchSize}/${allRouteIds.size}")
      migrateRouteElements(routeIds)
    }
  }

  private def findAllRouteIds(): Seq[Long] = {
    log.info("Collecting routeIds")
    routeRepository.allRouteIds()
  }

  private def migrateRoutes(routeIds: Seq[Long]): Unit = {
    val routeInfos = routeIds.flatMap { routeId =>
      routeRepository.routeWithId(routeId).map(routeInfo => routeInfo.copy(_id = routeInfo.id))
    }
    database.routes.insertMany(routeInfos)
    routeInfos.foreach(migrateNodeRouteRefs)
  }

  private def migrateRouteElements(routeIds: Seq[Long]): Unit = {
    val routeElementss = routeIds.flatMap { routeId =>
      routeRepository.routeElementsWithId(routeId).map(routeElements => routeElements.copy(_id = routeElements.routeId))
    }
    database.routeElements.insertMany(routeElementss)
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
      database.nodeRouteRefs.insertMany(refs)
    }
  }
}
