package kpn.server.analyzer.engine.changes.route

import kpn.api.common.Fact
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RawRouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileChangeAnalyzer
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeUpdateProcessor(
  analysisContext: AnalysisContext,
  rawDataRepository: RawDataRepository,
  routeRepository: RouteRepository,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  baseRouteDocBuilder: BaseRouteDocBuilder,
  routeTileChangeAnalyzer: RouteTileChangeAnalyzer,
  baseRouteDeleter: BaseRouteChangeDeleter,
) extends BaseRouteChangeSubProcessor {

  private val defaultLog = Log(classOf[BaseRouteChangeUpdateProcessor])

  def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    loggedProcess(changeSetContext, routeId, defaultLog)
  }

  def loggedProcess(changeSetContext: ChangeSetContext, routeId: Long, log: Log): ChangeSetContext = {
    rawDataRepository.route(changeSetContext.changeSet.timestampAfter, routeId) match {
      case Some(rawRouteDoc) => processRoute(changeSetContext, routeId, rawRouteDoc)
      case None => handleMissingRoute(changeSetContext, routeId, log)
    }
  }

  private def processRoute(changeSetContext: ChangeSetContext, routeId: Long, rawRouteDoc: RawRouteDoc): ChangeSetContext = {
    val beforeOption = routeRepository.findBaseRouteById(routeId)
    val context = baseRouteMainAnalyzer.analyze(rawRouteDoc.relation, rawRouteDoc.structure /* TODO rename to rawRouteDoc.hierarchy ??? */)
    if (context.abort) {
      handleAbortedRouteAnalysis(changeSetContext, context)
    }
    else {
      processRouteUpdate(changeSetContext, routeId, rawRouteDoc, beforeOption, context)
    }
  }

  private def handleAbortedRouteAnalysis(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext): ChangeSetContext = {
    if (context.facts.contains(Fact.RouteTagMissing)) {
      baseRouteDeleter.delete(changeSetContext, context.routeId)
    }
    else {
      changeSetContext
    }
  }

  private def processRouteUpdate(
    changeSetContext: ChangeSetContext,
    routeId: Long,
    rawRouteDoc: RawRouteDoc,
    beforeOption: Option[BaseRouteDoc],
    context: BaseRouteAnalysisContext
  ): ChangeSetContext = {

    val baseRouteDoc = baseRouteDocBuilder.build(context)
    analysisContext.watched.routes.add(rawRouteDoc.relation.id, context.elementIds)
    routeRepository.saveBaseRoute(baseRouteDoc)

    val beforeTileDocs = routeRepository.routeTiles(rawRouteDoc.relation.id)
    val afterTileDocs = RouteTileDocBuilder.build(context)
    val impactedTiles = routeTileChangeAnalyzer.impactedTiles(beforeTileDocs, afterTileDocs)

    afterTileDocs.foreach(routeRepository.saveRouteTile)

    val beforeNodeIds = beforeOption.toSeq.flatMap(_.nodes.nodeIds).toSet
    val afterNodeIds = baseRouteDoc.nodes.nodeIds.toSet
    val addedNodeIds = afterNodeIds -- beforeNodeIds
    val removedNodeIds = beforeNodeIds -- afterNodeIds
    val impactedNodeIds = (addedNodeIds ++ removedNodeIds).toSeq.sorted
    changeSetContext.withImpact(
      nodeIds = (addedNodeIds ++ removedNodeIds).toSeq.sorted,
      tileIds = impactedTiles,
      routeIds = Seq(routeId)
    )
  }

  private def handleMissingRoute(changeSetContext: ChangeSetContext, routeId: Long, log: Log): ChangeSetContext = {
    log.warn(s"overpass route $routeId not found")
    changeSetContext.withImpact(
      routeIds = Seq(routeId),
    )
  }
}
