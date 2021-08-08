package kpn.server.analyzer.full.route

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
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
  masterRouteAnalyzer: MasterRouteAnalyzer,
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
      val futures = Future.sequence(
        routeIds.sliding(batchSize, batchSize).zipWithIndex.map { case (batchRouteIds, index) =>
          Future(
            Log.context(s"${index * batchSize}/${routeIds.size}") {
              analyzeRouteBatch(timestamp, batchRouteIds)
            }
          )
        }.toSeq
      )
      val updatedRouteIds = Await.result(futures, Duration(2, TimeUnit.HOURS)).flatten
      (s"${updatedRouteIds.size} routes analyzed", updatedRouteIds)
    }
  }

  private def analyzeRouteBatch(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Long] = {
    log.infoElapsed {
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
      routeRepository.bulkSave(routeInfos)
      val ids = routeInfos.map(_.id)
      (s"processed ${ids.size} routes: ${ids.mkString(", ")}", ids)
    }
  }

  private def deactivateObsoleteRoutes(routeIds: Seq[Long]): Unit = {
    if (routeIds.nonEmpty) {
      routeIds.foreach { routeId =>
        routeRepository.findById(routeId).map { routeInfo =>
          log.warn(s"de-activating route ${routeInfo._id}")
          routeRepository.save(routeInfo.copy(active = false))
        }
      }
    }
  }

}
