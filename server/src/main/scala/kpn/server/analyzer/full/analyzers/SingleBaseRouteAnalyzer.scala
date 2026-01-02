package kpn.server.analyzer.full.analyzers

import kpn.api.common.ChangeType
import kpn.api.common.Relation
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.diff.WayInfo
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Timestamp
import kpn.core.doc.RouteRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileData
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.route.base.RouteGeometryAnalyzer
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteTileRepository
import org.springframework.stereotype.Component

@Component
class SingleBaseRouteAnalyzer(
  rawDataRepository: RawDataRepository,
  routeRepository: RouteRepository,
  routeTileRepository: RouteTileRepository,
  changeSetRepository: ChangeSetRepository,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  baseRouteDocBuilder: BaseRouteDocBuilder,
) {

  private val ThreadPoolSize = 10
  private val log = Log(classOf[SingleBaseRouteAnalyzer])

  private case class AnalysisResult(
    analyzedIds: Seq[Long],
    obsoleteIds: Seq[Long]
  )

  def processRoute(
    timestamp: Timestamp,
    initialAnalysisChangeSetContext: Option[ChangeSetContext],
    routeId: Long
  ): Unit = {
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

  private def analyzeBaseRoute(
    initialAnalysisChangeSetContext: Option[ChangeSetContext],
    relation: Relation,
    subRelationTree: Option[RouteRelation]
  ): Unit = {
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

  private def baseRouteChange(
    initialAnalysisChangeSetContext: Option[ChangeSetContext],
    context: BaseRouteAnalysisContext
  ): Option[BaseRouteChange] = {
    initialAnalysisChangeSetContext.flatMap { changeSetContext =>
      val addedWays = context.relation.members.flatMap(_.way.map(WayInfo.from))
      if (addedWays.nonEmpty) {
        val wayDiffsInfo = Some(
          WayDiffsInfo(
            removed = Seq.empty,
            added = addedWays,
            updated = Seq.empty
          )
        )
        val key = changeSetContext.buildChangeKey(context.routeId)

        val geometryDiff = new RouteGeometryAnalyzer().initialAnalyze(context.relation)

        Some(
          BaseRouteChange(
            key.toId,
            key = key,
            changeType = ChangeType.InitialValue,
            routeDiff = RouteDiff.empty,
            wayDiffs = wayDiffsInfo,
            geometryDiff = geometryDiff
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
      val doc = buildRouteTileInfo(context, tileData)
      routeTileRepository.saveRouteTile(doc)
    }
  }

  private def buildRouteTileInfo(context: BaseRouteAnalysisContext, tileData: RouteTileData) = {
    RouteTileInfo(
      _id = s"${tileData.name}-${context.relation.id}",
      routeId = context.relation.id,
      routeName = context.routeNameAnalysis.name.getOrElse("no-name"),
      routeTypes = context.routeTypes,
      z = tileData.z,
      x = tileData.x,
      y = tileData.y,
      layer = tileData.layer,
      scope = tileData.scope,
      survey = tileData.survey,
      error = tileData.error,
      proposed = tileData.proposed,
      segments = tileData.segments
    )
  }
}
