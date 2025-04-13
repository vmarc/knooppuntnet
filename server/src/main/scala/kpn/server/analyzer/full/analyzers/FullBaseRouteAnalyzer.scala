package kpn.server.analyzer.full.analyzers

import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.doc.RouteRelation
import kpn.core.util.Log
import kpn.core.util.ThreadExecutor
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class FullBaseRouteAnalyzer(
  rawDataRepository: RawDataRepository,
  routeRepository: RouteRepository,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
) {

  private val log = Log(classOf[FullBaseRouteAnalyzer])

  def analyze(context: FullAnalysisContext): FullAnalysisContext = {
    Log.context("base routes") {
      log.infoElapsed {
        val existingRouteIds = collectActiveBaseRouteIds()
        val routeIds = collectRawRouteIds(context.timestamp)
        analyzeRoutes(context.timestamp, routeIds)
        val obsoleteRouteIds = (existingRouteIds.toSet -- routeIds).toSeq.sorted
        deactivateObsoleteRoutes(obsoleteRouteIds)
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
      ThreadExecutor.execute(10, routeIds) { (index, count, routeId) =>
        Log.context(context) {
          Log.context(s"$index/$count $routeId") {
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
        }
      }
      (s"Analyzed $routeCount routes", ())
    }
  }

  private def analyzeBaseRoute(relation: Relation, hierarchy: Option[RouteRelation]): Unit = {
    val context = baseRouteMainAnalyzer.analyze(relation, hierarchy)
    if (!context.abort) {
      val baseRouteDoc = new BaseRouteDocBuilder(context).build()
      routeRepository.saveBaseRoute(baseRouteDoc)
      context.tileDatas.foreach { tileData =>
        val doc = RouteTileDoc(
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
        routeRepository.saveRouteTile(doc)
      }
      // TODO saveRouteChange(routeAnalysis)
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
