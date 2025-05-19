package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Fact
import kpn.core.doc.BaseRouteDoc
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
      val context = baseRouteMainAnalyzer.analyze(rawRouteDoc.relation, rawRouteDoc.subRelationTree)
      if (context.abort) {
        handleAbortedRouteAnalysis(changeSetContext, context)
      }
      else {
        val beforeOption = routeRepository.findBaseRouteById(routeId)
        processRouteUpdate(changeSetContext, beforeOption, context)
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
      beforeOption: Option[BaseRouteDoc],
      context: BaseRouteAnalysisContext
    ): ChangeSetContext = {

      analysisContext.watched.routes.add(routeId, context.elementIds)

      val baseRouteDoc = baseRouteDocBuilder.build(context)
      routeRepository.saveBaseRoute(baseRouteDoc)

      val updatedChangeSetContext1 = baseRouteChangeUpdateTileProcessor.process(changeSetContext, context)
      val updatedChangeSetContext2 = processWayUpdates(updatedChangeSetContext1, beforeOption, baseRouteDoc)

      val beforeNodeIds = beforeOption.toSeq.flatMap(_.nodes.nodeIds).toSet
      val afterNodeIds = baseRouteDoc.nodes.nodeIds.toSet
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
    beforeOption: Option[BaseRouteDoc],
    afterBaseRouteDoc: BaseRouteDoc
  ): ChangeSetContext = {

    val beforeRelationOption = beforeOption.flatMap(_.relation)
    val afterRelationOption = afterBaseRouteDoc.relation

    (beforeRelationOption, afterRelationOption) match {
      case (Some(before), Some(after)) =>
        baseRouteChangeUpdateWayProcessor.process(changeSetContext, before, after)
      case _ => changeSetContext
    }
  }
}
