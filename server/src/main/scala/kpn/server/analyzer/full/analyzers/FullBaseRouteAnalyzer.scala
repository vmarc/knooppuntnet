package kpn.server.analyzer.full.analyzers

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.core.util.ThreadExecutor
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class FullBaseRouteAnalyzer(
  rawDataRepository: RawDataRepository,
  routeRepository: RouteRepository,
  baseRouteDocBuilder: BaseRouteDocBuilder,
  singleBaseRouteAnalyzer: SingleBaseRouteAnalyzer
) extends FullAnalyzer {

  private val ThreadPoolSize = 10
  private val log = Log(classOf[FullBaseRouteAnalyzer])

  private case class AnalysisResult(
    analyzedIds: Seq[Long],
    obsoleteIds: Seq[Long]
  )

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("base routes") {
      log.infoElapsed {
        val result = analyzeAll(context)
        (message(result), context)
      }
    }
  }

  private def analyzeAll(context: FullAnalysisContext): AnalysisResult = {
    val existingRouteIds = collectActiveBaseRouteIds()
    val routeIds = collectRawRouteIds(context.timestamp)
    analyzeRoutes(context.timestamp, context.initialAnalysisChangeSetContext, routeIds)
    val obsoleteRouteIds = handleObsoleteRoutes(existingRouteIds, routeIds)
    AnalysisResult(
      routeIds,
      obsoleteRouteIds
    )
  }

  private def collectActiveBaseRouteIds(): Seq[Long] = {
    log.info(s"Collecting active route ids")
    log.infoElapsed {
      val ids = routeRepository.activeBaseRouteIds()
      (s"${ids.size} active route ids", ids)
    }
  }

  private def collectRawRouteIds(timestamp: Timestamp): Seq[Long] = {
    log.info(s"Collecting raw route ids")
    log.infoElapsed {
      val ids = rawDataRepository.routeIds(timestamp)
      (s"${ids.size} raw route ids", ids)
    }
  }

  private def analyzeRoutes(timestamp: Timestamp, initialAnalysisChangeSetContext: Option[ChangeSetContext], routeIds: Seq[Long]): Unit = {
    val routeCount = routeIds.size
    log.info(s"analyzing $routeCount base routes")
    val context = Log.contextMessages
    log.infoElapsed {
      ThreadExecutor.execute(ThreadPoolSize, routeIds) { (index, count, routeId) =>
        Log.context(context) {
          Log.context(s"$index/$count $routeId") {
            processRoute(timestamp, initialAnalysisChangeSetContext, routeId)
          }
        }
      }
      (s"Analyzed $routeCount routes", ())
    }
  }

  private def processRoute(timestamp: Timestamp, initialAnalysisChangeSetContext: Option[ChangeSetContext], routeId: Long): Unit = {
    singleBaseRouteAnalyzer.processRoute(timestamp, initialAnalysisChangeSetContext, routeId)
  }

  private def saveBaseRoute(context: BaseRouteAnalysisContext): Unit = {
    routeRepository.saveBaseRoute(baseRouteDocBuilder.build(context))
  }

  private def handleObsoleteRoutes(existingRouteIds: Seq[Long], routeIds: Seq[Long]) = {
    val obsoleteRouteIds = (existingRouteIds.toSet -- routeIds).toSeq.sorted
    deactivateObsoleteRoutes(obsoleteRouteIds)
    obsoleteRouteIds
  }

  private def deactivateObsoleteRoutes(routeIds: Seq[Long]): Unit = {
    routeIds.foreach { routeId =>
      deactivateRoute(routeId)
      deactivateBaseRoute(routeId)
    }
  }

  private def deactivateRoute(routeId: Long): Unit = {
    routeRepository.findRouteById(routeId).foreach { routeDoc =>
      log.warn(s"de-activating route ${routeDoc._id}")
      routeRepository.saveRoute(routeDoc.deactivated)
    }
  }

  private def deactivateBaseRoute(routeId: Long): Unit = {
    routeRepository.findBaseRouteById(routeId).foreach { baseRouteDoc =>
      log.warn(s"de-activating route ${baseRouteDoc._id}")
      routeRepository.saveBaseRoute(baseRouteDoc.deactivated)
    }
  }

  private def message(result: AnalysisResult): String = {
    s"Analyzed (${result.analyzedIds.size} routes, ${result.obsoleteIds.size} obsolete routes)"
  }
}
