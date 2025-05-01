package kpn.server.analyzer.full.analyzers

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class FullRouteAnalyzer(
  routeRepository: RouteRepository,
  routeMainAnalyzer: RouteMainAnalyzer,
  initialRouteChangeBuilder: InitialRouteChangeBuilder
) extends FullAnalyzer {

  private val log = Log(classOf[FullRouteAnalyzer])

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("full-route-analysis") {
      log.infoElapsed {
        val existingRouteIds = collectActiveRouteIds()
        val routeIds = collectBaseRouteIds()
        val analyzedRouteIds = analyzeRoutes(context, routeIds)
        val obsoleteRouteIds = findObsoleteRoutes(existingRouteIds, analyzedRouteIds)
        deactivateObsoleteRoutes(obsoleteRouteIds)
        val message = s"completed (${analyzedRouteIds.size} routes, ${obsoleteRouteIds.size} obsolete routes)"
        (message, context)
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

  private def analyzeRoutes(context: FullAnalysisContext, routeIds: Seq[Long]): Seq[Long] = {
    log.infoElapsed {
      val routeIdsSize = routeIds.size
      val ids = routeIds.zipWithIndex.flatMap { case (relationId, index) =>
        Log.context(s"${index + 1}/$routeIdsSize route=$relationId") {
          analyzeRoute(context, relationId)
        }
      }
      (s"${ids.size} routes analyzed", ids)
    }
  }

  private def analyzeRoute(context: FullAnalysisContext, relationId: Long) = {
    try {
      log.info("analyze main")
      routeRepository.findBaseRouteById(relationId) match {
        case None =>
          log.error(s"could not find route details")
          None
        case Some(baseRouteDoc) =>
          routeMainAnalyzer.analyze(baseRouteDoc).map { routeDoc =>
            routeRepository.saveRoute(routeDoc)
            context.initialAnalysisChangeSetContext.foreach { changeSetContext =>
              initialRouteChangeBuilder.saveRouteChange(changeSetContext, routeDoc)
            }
            relationId
          }
      }
    } catch {
      case e: Exception =>
        log.error(s"Error analyzing main route $relationId", e)
        None
    }
  }

  private def findObsoleteRoutes(existingRouteIds: Seq[Long], analyzedRouteIds: Seq[Long]): Seq[Long] = {
    (existingRouteIds.toSet -- analyzedRouteIds).toSeq.sorted
  }

  private def deactivateObsoleteRoutes(routeIds: Seq[Long]): Unit = {
    routeIds.foreach { routeId =>
      deactivateBaseRoute(routeId)
      deactivateRoute(routeId)
    }
  }

  private def deactivateBaseRoute(routeId: Long): Unit = {
    routeRepository.findBaseRouteById(routeId).foreach { baseRouteDoc =>
      log.warn(s"de-activating route ${baseRouteDoc._id}")
      routeRepository.saveBaseRoute(baseRouteDoc.deactivated)
    }
  }

  private def deactivateRoute(routeId: Long): Unit = {
    routeRepository.findRouteById(routeId).foreach { routeDoc =>
      log.warn(s"de-activating route ${routeDoc._id}")
      routeRepository.saveRoute(routeDoc.deactivated)
    }
  }
}
