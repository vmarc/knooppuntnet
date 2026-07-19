package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Bounds
import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.diff.WayInfo
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.route.GeometryDiff
import kpn.api.common.route.WayGeometryUpdate
import kpn.api.common.route.WayLine
import kpn.core.builders.WayLineUtil
import kpn.core.doc.RawRouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteTileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class BaseRouteChangeCreateProcessor(
  analysisContext: AnalysisContext,
  rawDataRepository: RawDataRepository,
  routeRepository: RouteRepository,
  routeTileRepository: RouteTileRepository,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  baseRouteDocBuilder: BaseRouteDocBuilder,
  @Autowired(required = false)
  log: Log = Log(classOf[BaseRouteChangeCreateProcessor])
) extends BaseRouteChangeSubProcessor {

  def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    val updatedChangeSetContext = rawDataRepository.route(changeSetContext.changeSet.timestampAfter, routeId) match {
      case Some(rawRouteDoc) => processRoute(changeSetContext, rawRouteDoc)
      case None =>
        log.warn(s"overpass route $routeId not found")
        changeSetContext
    }
    updatedChangeSetContext.withImpact(
      routeIds = Seq(routeId),
    )
  }

  private def processRoute(changeSetContext: ChangeSetContext, rawRouteDoc: RawRouteDoc): ChangeSetContext = {
    val context = baseRouteMainAnalyzer.analyze(rawRouteDoc.relation, rawRouteDoc.subRelationTree)
    if (context.abort) {
      handleAbortedRouteAnalysis(changeSetContext, context)
    }
    else {
      processRouteAnalysisResult(changeSetContext, context)
    }
  }

  private def processRouteAnalysisResult(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext): ChangeSetContext = {
    val changeSetContext1 = processRouteChange(changeSetContext, context)
    updateRouteData(context)
    changeSetContext1.withImpact(
      tileIds = context.tiles,
      nodeIds = context.routeNodesAnalysis.nodeIds,
    )
  }

  private def processRouteChange(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext): ChangeSetContext = {

    val wayDiffs = if (context.relation.ways.nonEmpty) {
      val added = context.relation.ways.map(WayInfo.from)
      Some(
        WayDiffsInfo(
          added = added,
        )
      )
    }
    else {
      None
    }

    val geometryDiff = if (context.relation.ways.nonEmpty) {
      val bounds = Bounds.from(context.relation.ways.flatMap(_.nodes))
      val added = context.relation.ways.map { way =>
        val wayLine = WayLineUtil.fromLatLons(way.nodes)
        WayGeometryUpdate(
          wayId = way.id,
          common = None,
          added = Some(Seq(wayLine)),
          removed = None
        )
      }
      Some(
        GeometryDiff(
          common = Seq.empty,
          update = added,
          bounds
        )
      )
    }
    else {
      None
    }

    val key = changeSetContext.buildChangeKey(context.routeId)
    val change = BaseRouteChange(
      _id = key.toId,
      key = key,
      changeType = ChangeType.Create,
      before = None,
      after = Some(context.relation.toMeta),
      routeDiff = RouteDiff.empty,
      wayDiffs,
      geometryDiff
    )

    changeSetContext.copy(
      changes = changeSetContext.changes.copy(
        baseRouteChanges = changeSetContext.changes.baseRouteChanges :+ change,
      )
    )
  }

  private def handleAbortedRouteAnalysis(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext): ChangeSetContext = {
    if (context.facts.contains(Fact.LostRouteTags)) {
      val baseRouteDoc = baseRouteDocBuilder.build(context)
      changeSetContext.withImpact(
        tileIds = context.tiles,
        nodeIds = baseRouteDoc.base.nodes.nodeIds,
      )
    }
    else {
      changeSetContext
    }
  }

  private def updateRouteData(context: BaseRouteAnalysisContext): Unit = {
    updateWatchedRoutes(context)
    updateBaseRouteDoc(context)
    updateRouteTileInfos(context)
  }

  private def updateBaseRouteDoc(context: BaseRouteAnalysisContext): Unit = {
    val baseRouteDoc = baseRouteDocBuilder.build(context)
    routeRepository.saveBaseRoute(baseRouteDoc)
  }

  private def updateRouteTileInfos(context: BaseRouteAnalysisContext): Unit = {
    val routeTileInfos = RouteTileInfoBuilder.build(context)
    routeTileInfos.foreach(routeTileRepository.saveRouteTile)
  }

  private def updateWatchedRoutes(context: BaseRouteAnalysisContext): Unit = {
    analysisContext.watched.routes.add(context.relation.id, context.elementIds)
  }
}
