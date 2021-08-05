package kpn.server.analyzer.full.route

import kpn.api.common.route.RouteInfo
import kpn.api.custom.Timestamp
import kpn.core.mongo.Database
import kpn.core.mongo.actions.routes.MongoQueryRouteIds
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.full.FullAnalysisContext
import kpn.server.overpass.OverpassRepository
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.ReplaceOneModel
import org.mongodb.scala.model.ReplaceOptions
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

@Component
class FullRouteAnalyzerImpl(
  database: Database,
  overpassRepository: OverpassRepository,
  masterRouteAnalyzer: MasterRouteAnalyzer,
  implicit val analysisExecutionContext: ExecutionContext
) extends FullRouteAnalyzer {

  private val log = Log(classOf[FullRouteAnalyzerImpl])

  override def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    val existingRouteIds = collectActiveRouteIds()
    val routeIds = collectOverpassRouteIds(context.timestamp).take(5)
    val analyzedRouteIds = analyzeRoutes(context.timestamp, routeIds)
    val obsoleteRouteIds = (existingRouteIds.toSet -- analyzedRouteIds).toSeq.sorted
    deactivateObsoleteRoutes(obsoleteRouteIds)
    context
  }

  private def collectActiveRouteIds(): Seq[Long] = {
    log.info(s"Collecting active route ids")
    log.infoElapsed {
      val ids = new MongoQueryRouteIds(database).execute(log).sorted
      (s"${ids.size} mongodb route ids", ids)
    }
  }

  private def collectOverpassRouteIds(timestamp: Timestamp): Seq[Long] = {
    log.info(s"Collecting overpass route ids")
    log.infoElapsed {
      val ids = overpassRepository.routeIds(timestamp)
      (s"${ids.size} overpass route ids", ids)
    }
  }

  private def analyzeRoutes(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Long] = {
    log.infoElapsed {
      val batchSize = 100
      val futures = Future.sequence(
        routeIds.sliding(batchSize, batchSize).zipWithIndex.map { case (batchRouteIds, index) =>
          Future(
            Log.context(s"${index * batchSize}/${routeIds.size}") {
              analyzerRouteBatch(timestamp, batchRouteIds)
            }
          )
        }.toSeq
      )
      val updatedRouteIds = Await.result(futures, Duration(2, TimeUnit.HOURS)).flatten
      (s"${updatedRouteIds.size} routes analyzed", updatedRouteIds)
    }
  }

  private def analyzerRouteBatch(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Long] = {

    val relations = overpassRepository.fullRelations(timestamp, routeIds)
    val routeInfos = relations.flatMap { relation =>
      Log.context(s"route=${relation.id}") {
        try {
          masterRouteAnalyzer.analyze(relation).map(_.route)
        } catch {
          case e: Exception =>
            log.error(s"Error processing route ${relation.id}", e)
            throw e
        }
      }
    }
    log.infoElapsed {
      val requests = routeInfos.map { doc =>
        ReplaceOneModel[RouteInfo](Filters.equal("_id", doc._id), doc, ReplaceOptions().upsert(true))
      }
      val future = database.routes.native.bulkWrite(requests).toFuture()
      Await.result(future, Duration(5, TimeUnit.MINUTES))
      (s"${requests.size} routes saved", ())
    }
    relations.map(_.id)
  }

  private def deactivateObsoleteRoutes(routeIds: Seq[Long]): Unit = {
  }

}
