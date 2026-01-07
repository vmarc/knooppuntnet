package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Fact
import kpn.core.doc.RawRouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeUpdateProcessor(
  analysisContext: AnalysisContext,
  rawDataRepository: RawDataRepository,
  routeRepository: RouteRepository,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  baseRouteDocBuilder: BaseRouteDocBuilder,
  baseRouteDiffAnalyzer: BaseRouteDiffAnalyzer,
  baseRouteChangeUpdateTileProcessor: BaseRouteChangeUpdateTileProcessor,
  baseRouteChangeDeleter: BaseRouteChangeDeleter,
  @Autowired(required = false)
  log: Log = Log(classOf[BaseRouteChangeUpdateProcessor])
) extends BaseRouteChangeSubProcessor {

  def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    val updatedChangeSetContext = rawDataRepository.route(changeSetContext.changeSet.timestampAfter, routeId) match {
      case Some(rawRouteDoc) => processRoute(changeSetContext, rawRouteDoc, routeId)
      case None =>
        log.warn(s"overpass route $routeId not found")
        changeSetContext
    }
    updatedChangeSetContext.withImpact(
      routeIds = Seq(routeId),
    )
  }

  private def processRoute(changeSetContext: ChangeSetContext, rawRouteDoc: RawRouteDoc, routeId: Long): ChangeSetContext = {
    val afterContext = baseRouteMainAnalyzer.analyze(rawRouteDoc.relation, rawRouteDoc.subRelationTree)
    if (afterContext.abort) {
      handleAbortedRouteAnalysis(changeSetContext, afterContext, routeId)
    }
    else {
      rawDataRepository.route(changeSetContext.changeSet.timestampBefore, routeId) match {
        case None =>
          log.warn(s"overpass before route $routeId not found at ${changeSetContext.changeSet.timestampBefore.yyyymmddhhmmss}")
          changeSetContext
        case Some(beforeRawRouteDoc) =>
          val beforeContext = baseRouteMainAnalyzer.analyze(beforeRawRouteDoc.relation, beforeRawRouteDoc.subRelationTree)
          if (beforeContext.abort) {
            throw new IllegalStateException("properly handle this situation, just as if the route is newly created")
            // handleAbortedRouteAnalysis(changeSetContext, afterContext, routeId)
          }
          else {
            processRouteUpdate(changeSetContext, beforeContext, afterContext, routeId)
          }
      }
    }
  }

  private def handleAbortedRouteAnalysis(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext, routeId: Long): ChangeSetContext = {
    if (context.facts.contains(Fact.RouteTagMissing)) {
      baseRouteChangeDeleter.delete(changeSetContext, routeId)
    }
    else {
      changeSetContext
    }
  }

  private def processRouteUpdate(
    changeSetContext: ChangeSetContext,
    beforeContext: BaseRouteAnalysisContext,
    afterContext: BaseRouteAnalysisContext,
    routeId: Long
  ): ChangeSetContext = {

    analysisContext.watched.routes.add(routeId, afterContext.elementIds)

    val baseRouteDoc = baseRouteDocBuilder.build(afterContext)
    routeRepository.saveBaseRoute(baseRouteDoc)

    val updatedChangeSetContext1 = baseRouteDiffAnalyzer.analyze(changeSetContext, beforeContext, afterContext)
    val updatedChangeSetContext2 = baseRouteChangeUpdateTileProcessor.process(updatedChangeSetContext1, afterContext)
    determineImpactedNodes(updatedChangeSetContext2, beforeContext, afterContext)
  }

  private def determineImpactedNodes(
    changeSetContext: ChangeSetContext,
    beforeContext: BaseRouteAnalysisContext,
    afterContext: BaseRouteAnalysisContext
  ): ChangeSetContext = {

    val beforeNodeIds = beforeContext.routeNodesAnalysis.nodeIds.toSet
    val afterNodeIds = afterContext.routeNodesAnalysis.nodeIds.toSet
    val addedNodeIds = afterNodeIds -- beforeNodeIds
    val removedNodeIds = beforeNodeIds -- afterNodeIds
    val impactedNodeIds = (addedNodeIds ++ removedNodeIds).toSeq.sorted
    changeSetContext.withImpact(
      nodeIds = (addedNodeIds ++ removedNodeIds).toSeq.sorted,
    )
  }
}
