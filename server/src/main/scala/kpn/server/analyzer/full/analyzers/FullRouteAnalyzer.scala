package kpn.server.analyzer.full.analyzers

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class FullRouteAnalyzer(
  routeRepository: RouteRepository,
  routeMainAnalyzer: RouteMainAnalyzer,
) {

  private val log = Log(classOf[FullRouteAnalyzer])

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("full-route-analysis") {
      log.infoElapsed {
        val existingRouteIds = collectActiveRouteIds()
        val routeIds = collectBaseRouteIds()
        val analyzedRouteIds = analyzeRoutes(routeIds)
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

  private def collectBaseRouteIds(): Seq[Long] = {
    log.info(s"Collecting base route ids")
    log.infoElapsed {
      val ids = routeRepository.activeBaseRouteIds()
      (s"${ids.size} base route ids", ids)
    }
  }

  private def analyzeRoutes(routeIds: Seq[Long]): Seq[Long] = {
    log.infoElapsed {
      val routeIdsSize = routeIds.size
      val ids = routeIds.zipWithIndex.flatMap { case (relationId, index) =>
        Log.context(s"${index + 1}/$routeIdsSize route=$relationId") {
          try {
            log.info("analyze main")
            routeRepository.findBaseRouteById(relationId) match {
              case None =>
                log.error(s"could not find route details")
                None
              case Some(baseRouteDoc) =>
                routeMainAnalyzer.analyze(baseRouteDoc) match {
                  case Some(routeDoc) =>
                    routeRepository.saveRoute(routeDoc)
                    Some(relationId)
                  case None => None
                }
            }
          } catch {
            case e: Exception =>
              log.error(s"Error analyzing main route $relationId", e)
              None
          }
        }
      }
      (s"${ids.size} routes analyzed", ids)
    }
  }

  private def deactivateObsoleteRoutes(routeIds: Seq[Long]): Unit = {
    if (routeIds.nonEmpty) {
      routeIds.foreach { routeId =>
        routeRepository.findRouteById(routeId).foreach { routeDoc =>
          log.warn(s"de-activating route ${routeDoc._id}")
          routeRepository.saveRoute(routeDoc.deactivated)
        }
        routeRepository.findBaseRouteById(routeId).foreach { baseRouteDoc =>
          log.warn(s"de-activating route ${baseRouteDoc._id}")
          routeRepository.saveBaseRoute(baseRouteDoc.deactivated)
        }
      }
    }
  }
}
