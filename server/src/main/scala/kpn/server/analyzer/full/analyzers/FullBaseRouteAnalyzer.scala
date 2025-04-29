package kpn.server.analyzer.full.analyzers

import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.doc.RouteRelation
import kpn.core.util.Log
import kpn.core.util.ThreadExecutor
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileData
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class FullBaseRouteAnalyzer(
  rawDataRepository: RawDataRepository,
  routeRepository: RouteRepository,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
) extends FullAnalyzer {

  private val ThreadPoolSize = 10
  private val log = Log(classOf[FullBaseRouteAnalyzer])

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("base routes") {
      log.infoElapsed {
        val existingRouteIds = collectActiveBaseRouteIds()
        val routeIds = collectRawRouteIds(context.timestamp)
        analyzeRoutes(context.timestamp, routeIds)
        val obsoleteRouteIds = handleObsoleteRoutes(existingRouteIds, routeIds)
        (s"Analyzed (${routeIds.size} routes, ${obsoleteRouteIds.size} obsolete routes)", context)
      }
    }
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

  private def analyzeRoutes(timestamp: Timestamp, routeIds: Seq[Long]): Unit = {
    val routeCount = routeIds.size
    log.info(s"analyzing $routeCount base routes")
    val context = Log.contextMessages
    log.infoElapsed {
      ThreadExecutor.execute(ThreadPoolSize, routeIds) { (index, count, routeId) =>
        Log.context(context) {
          Log.context(s"$index/$count $routeId") {
            processRoute(timestamp, routeId)
          }
        }
      }
      (s"Analyzed $routeCount routes", ())
    }
  }

  private def processRoute(timestamp: Timestamp, routeId: Long): Unit = {
    log.infoElapsed {
      try {
        rawDataRepository.route(timestamp, routeId) match {
          case Some(rawRouteDoc) =>
            analyzeBaseRoute(rawRouteDoc.relation, rawRouteDoc.structure)
          case None =>
            log.error(s"route $routeId not found in route-relations")
        }
      } catch {
        case e: Exception =>
          log.error(s"Error analyzing detail route $routeId", e)
      }
      (s"Analyzed route $routeId", ())
    }
  }

  private def analyzeBaseRoute(relation: Relation, hierarchy: Option[RouteRelation]): Unit = {
    val context = baseRouteMainAnalyzer.analyze(relation, hierarchy)
    if (!context.abort) {
      saveRouteData(context)
    }
  }

  private def saveRouteData(context: BaseRouteAnalysisContext): Unit = {
    val baseRouteDoc = new BaseRouteDocBuilder(context).build()
    routeRepository.saveBaseRoute(baseRouteDoc)
    saveTileData(context)
  }

  private def saveTileData(context: BaseRouteAnalysisContext): Unit = {
    context.tileDatas.foreach { tileData =>
      val doc = buildTileDoc(context, tileData)
      routeRepository.saveRouteTile(doc)
    }
  }

  private def buildTileDoc(context: BaseRouteAnalysisContext, tileData: RouteTileData) = {
    RouteTileDoc(
      _id = s"${tileData.name}-${context.relation.id}",
      routeId = context.relation.id,
      routeName = context.routeNameAnalysis.name.getOrElse("no-name"), // TODO redesign tiles - can do better?
      routeTypes = context.routeTypes,
      z = tileData.z,
      x = tileData.x,
      y = tileData.y,
      layer = tileData.layer,
      scope = tileData.scope,
      survey = tileData.survey,
      error = tileData.error,
      segments = tileData.segments
    )
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
}
