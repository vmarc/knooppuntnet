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
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeUpdateProcessor(
  analysisContext: AnalysisContext,
  rawDataRepository: RawDataRepository,
  routeRepository: RouteRepository,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  baseRouteDocBuilder: BaseRouteDocBuilder,
  baseRouteChangeUpdateWayProcessor: BaseRouteChangeUpdateWayProcessor,
  baseRouteChangeUpdateTileProcessor: BaseRouteChangeUpdateTileProcessor,
  baseRouteDeleter: BaseRouteChangeDeleter,
) extends BaseRouteChangeSubProcessor {

  private val defaultLog = Log(classOf[BaseRouteChangeUpdateProcessor])

  def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    loggedProcess(defaultLog, changeSetContext, routeId)
  }

  def loggedProcess(log: Log, changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    new Updater(log, routeId).update(changeSetContext)
  }

  private class Updater(log: Log, routeId: Long) {

    def update(changeSetContext: ChangeSetContext): ChangeSetContext = {
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
      val afterContext = baseRouteMainAnalyzer.analyze(rawRouteDoc.relation, rawRouteDoc.subRelationTree)
      if (afterContext.abort) {
        handleAbortedRouteAnalysis(changeSetContext, afterContext)
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
              handleAbortedRouteAnalysis(changeSetContext, afterContext)
            }
            else {
              processRouteUpdate(changeSetContext, beforeContext, afterContext)
            }
        }
      }
    }

    private def handleAbortedRouteAnalysis(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext): ChangeSetContext = {
      if (context.facts.contains(Fact.RouteTagMissing)) {
        baseRouteDeleter.delete(changeSetContext, routeId)
      }
      else {
        changeSetContext
      }
    }

    private def processRouteUpdate(
      changeSetContext: ChangeSetContext,
      beforeContext: BaseRouteAnalysisContext,
      afterContext: BaseRouteAnalysisContext
    ): ChangeSetContext = {

      analysisContext.watched.routes.add(routeId, afterContext.elementIds)

      val baseRouteDoc = baseRouteDocBuilder.build(afterContext)
      routeRepository.saveBaseRoute(baseRouteDoc)

      val updatedChangeSetContext1 = baseRouteChangeUpdateTileProcessor.process(changeSetContext, afterContext)
      val updatedChangeSetContext2 = processWayUpdates(updatedChangeSetContext1, beforeContext, afterContext)

      val beforeNodeIds = beforeContext.routeNodesAnalysis.nodeIds.toSet
      val afterNodeIds = afterContext.routeNodesAnalysis.nodeIds.toSet
      val addedNodeIds = afterNodeIds -- beforeNodeIds
      val removedNodeIds = beforeNodeIds -- afterNodeIds
      val impactedNodeIds = (addedNodeIds ++ removedNodeIds).toSeq.sorted
      updatedChangeSetContext2.withImpact(
        nodeIds = (addedNodeIds ++ removedNodeIds).toSeq.sorted,
      )
    }
  }

  private def processWayUpdates(
    changeSetContext: ChangeSetContext,
    before: BaseRouteAnalysisContext,
    after: BaseRouteAnalysisContext
  ): ChangeSetContext = {
    baseRouteChangeUpdateWayProcessor.process(
      changeSetContext,
      before,
      after
    )
  }
}
