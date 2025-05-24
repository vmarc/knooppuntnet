package kpn.server.analyzer.full.analyzers

import kpn.api.common.Bounds
import kpn.api.common.ChangeType
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.diff.WayInfo
import kpn.api.common.route.GeometryDiff
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
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.route.base.RouteGeometryAnalyzer
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class FullBaseRouteAnalyzer(
  rawDataRepository: RawDataRepository,
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  baseRouteDocBuilder: BaseRouteDocBuilder,
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
    log.infoElapsed {
      try {
        rawDataRepository.route(timestamp, routeId) match {
          case Some(rawRouteDoc) =>
            analyzeBaseRoute(initialAnalysisChangeSetContext, rawRouteDoc.relation, rawRouteDoc.subRelationTree)
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

  private def analyzeBaseRoute(initialAnalysisChangeSetContext: Option[ChangeSetContext], relation: Relation, subRelationTree: Option[RouteRelation]): Unit = {
    val context = baseRouteMainAnalyzer.analyze(relation, subRelationTree)
    if (!context.abort) {
      saveBaseRoute(context)
      saveTileData(context)
      baseRouteChange(initialAnalysisChangeSetContext, context).foreach(changeSetRepository.saveBaseRouteChange)
    }
  }

  private def saveBaseRoute(context: BaseRouteAnalysisContext): Unit = {
    routeRepository.saveBaseRoute(baseRouteDocBuilder.build(context))
  }

  private def baseRouteChange(initialAnalysisChangeSetContext: Option[ChangeSetContext], context: BaseRouteAnalysisContext): Option[BaseRouteChange] = {
    initialAnalysisChangeSetContext.flatMap { changeSetContext =>
      val addedWays = context.relation.wayMembers.map(m => WayInfo.from(m.way))
      if (addedWays.nonEmpty) {
        val wayDiffsInfo = Some(
          WayDiffsInfo(
            removed = Seq.empty,
            added = addedWays,
            updated = Seq.empty
          )
        )
        val key = changeSetContext.buildChangeKey(context.routeId)

        val (geometryDiff: GeometryDiff, bounds: Bounds) = new RouteGeometryAnalyzer().initialAnalyze(context.relation)

        Some(
          BaseRouteChange(
            key.toId,
            key = key,
            changeType = ChangeType.InitialValue,
            wayDiffs = wayDiffsInfo,
            Some(geometryDiff),
            Some(bounds)
          )
        )
      }
      else {
        None
      }
    }
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

  private def message(result: AnalysisResult): String = {
    s"Analyzed (${result.analyzedIds.size} routes, ${result.obsoleteIds.size} obsolete routes)"
  }
}
