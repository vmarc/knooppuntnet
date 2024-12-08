package kpn.server.analyzer.full.route

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.RouteDetailDocBuilder
import kpn.server.analyzer.engine.analysis.route.RouteDetailMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteMainAnalyzer
import kpn.server.analyzer.full.FullAnalysisContext
import kpn.server.overpass.OverpassRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration

@Component
class FullRouteAnalyzerImpl(
  overpassRepository: OverpassRepository,
  routeRepository: RouteRepository,
  routeDetailMainAnalyzer: RouteDetailMainAnalyzer,
  routeMainAnalyzer: RouteMainAnalyzer,
  implicit val analysisExecutionContext: ExecutionContext
) extends FullRouteAnalyzer {

  private val log = Log(classOf[FullRouteAnalyzerImpl])

  override def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("full-route-analysis") {
      log.infoElapsed {
        val existingRouteIds = collectActiveRouteIds()
        val routeIds = collectOverpassRouteIds(context.timestamp)
        val analyzedRouteIds = analyzeRoutes(context.timestamp, routeIds)
        val obsoleteRouteIds = (existingRouteIds.toSet -- analyzedRouteIds).toSeq.sorted
        deactivateObsoleteRoutes(obsoleteRouteIds)
        (s"completed (${analyzedRouteIds.size} routes, ${obsoleteRouteIds.size} obsolete routes)", context)
      }
    }
  }

  private def collectActiveRouteIds(): Seq[Long] = {
    log.info(s"Collecting active route ids")
    log.infoElapsed {
      val ids = routeRepository.activeRouteIds()
      (s"${ids.size} active route ids", ids)
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
      val routeIdsSize = routeIds.size
      val futures = Future.sequence(
        routeIds.sliding(batchSize, batchSize).toSeq.zipWithIndex.map { case (batchRouteIds, index) =>
          Future(
            Log.context(s"${index * batchSize}/$routeIdsSize") {
              analyzeRouteBatch(timestamp, batchRouteIds)
            }
          )
        }
      )
      val updatedRouteIds = Await.result(futures, Duration(2, TimeUnit.HOURS)).flatten
      (s"${updatedRouteIds.size} routes analyzed", updatedRouteIds)
    }
  }

  private def analyzeRouteBatch(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Long] = {
    log.infoElapsed {
      val relations = overpassRepository.fullRelations(timestamp, routeIds)
      val routeDetailDocs = relations.flatMap { relation =>
        Log.context(s"route=${relation.id}") {
          try {
            routeDetailMainAnalyzer.analyze(relation, None /* TODO redesign - hierarchy */).map { context =>
              new RouteDetailDocBuilder(context).build()
            }
          } catch {
            case e: Exception =>
              log.error(s"Error processing route ${relation.id}", e)
              throw e
          }
        }
      }

      val routeDocs = routeDetailDocs.flatMap { routeDetailDoc =>
        routeMainAnalyzer.analyze(routeDetailDoc)
      }

      routeRepository.bulkSaveRouteDetails(routeDetailDocs)
      routeRepository.bulkSaveRoutes(routeDocs)

      val ids = routeDetailDocs.map(_.id)
      (s"processed ${ids.size} routes: ${ids.mkString(", ")}", ids)
    }
  }

  private def deactivateObsoleteRoutes(routeIds: Seq[Long]): Unit = {
    if (routeIds.nonEmpty) {
      routeIds.foreach { routeId =>
        routeRepository.findRouteById(routeId).foreach { routeDoc =>
          log.warn(s"de-activating route ${routeDoc._id}")
          routeRepository.saveRoute(routeDoc.deactivated)
        }
        routeRepository.findRouteDetailById(routeId).foreach { routeDetailDoc =>
          log.warn(s"de-activating route ${routeDetailDoc._id}")
          routeRepository.saveRouteDetail(routeDetailDoc.deactivated)
        }
      }
    }
  }
}
