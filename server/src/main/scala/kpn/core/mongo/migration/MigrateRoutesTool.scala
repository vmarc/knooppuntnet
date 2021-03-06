package kpn.core.mongo.migration

import kpn.api.common.route.RouteInfo
import kpn.api.custom.ScopedNetworkType
import kpn.core.db.couch.Couch
import kpn.core.mongo.Database
import kpn.core.mongo.NodeRouteRef
import kpn.core.mongo.migration.MigrateRoutesTool.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLabelsAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute
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
      routeRepository.routeWithId(routeId).map { routeInfo =>
        val migratedNodeRefs = if (routeInfo.nodeRefs == null) {
          routeInfo.analysis.map.nodeIds
        }
        else {
          routeInfo.nodeRefs
        }

        val labels = generateRouteLabels(routeInfo)

        routeInfo.copy(
          _id = routeInfo.id,
          labels = labels,
          nodeRefs = migratedNodeRefs
        )
      }
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
      val _id = s"$nodeId:${routeInfo.summary.id}"
      NodeRouteRef(
        _id,
        nodeId,
        routeInfo.summary.id,
        routeInfo.summary.networkType,
        routeInfo.summary.name
      )
    }
    if (refs.nonEmpty) {
      database.nodeRouteRefs.insertMany(refs)
    }
  }

  private def generateRouteLabels(routeInfo: RouteInfo): Seq[String] = {
    val context = RouteAnalysisContext(
      new AnalysisContext(),
      LoadedRoute(
        routeInfo.summary.country,
        ScopedNetworkType(routeInfo.summary.networkScope, routeInfo.summary.networkType),
        null,
        null
      ),
      routeInfo.orphan,
      Map.empty,
      facts = routeInfo.facts,
      lastSurvey = routeInfo.lastSurvey,
      active = routeInfo.active,
      locationAnalysis = Some(routeInfo.analysis.locationAnalysis)
    )
    new RouteLabelsAnalyzer(context).analyze.labels
  }
}
