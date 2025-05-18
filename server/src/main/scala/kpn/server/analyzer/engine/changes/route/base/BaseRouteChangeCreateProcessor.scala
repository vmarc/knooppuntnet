package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.changes.details.BaseRouteChange
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
class BaseRouteChangeCreateProcessor(
  analysisContext: AnalysisContext,
  rawDataRepository: RawDataRepository,
  routeRepository: RouteRepository,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  baseRouteDocBuilder: BaseRouteDocBuilder
) extends BaseRouteChangeSubProcessor {

  private val defaultLog = Log(classOf[BaseRouteChangeCreateProcessor])

  def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    loggedProcess(defaultLog, changeSetContext, routeId)
  }

  def loggedProcess(log: Log, changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    new Creater(log, routeId).create(changeSetContext)
  }

  private class Creater(log: Log, routeId: Long) {

    def create(changeSetContext: ChangeSetContext): ChangeSetContext = {
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
      val context = baseRouteMainAnalyzer.analyze(rawRouteDoc.relation, rawRouteDoc.structure)
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

      val key = changeSetContext.buildChangeKey(routeId)
      val change = BaseRouteChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.Create,
        removedWays = Seq.empty,
        addedWays = context.relation.wayMembers.map(_.way.toRaw),
        updatedWays = Seq.empty,
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
          tileIds = baseRouteDoc.tiles,
          nodeIds = baseRouteDoc.nodes.nodeIds,
        )
      }
      else {
        changeSetContext
      }
    }

    private def updateRouteData(context: BaseRouteAnalysisContext): Unit = {
      updateWatchedRoutes(context)
      updateBaseRouteDoc(context)
      updateRouteTileDocs(context)
    }

    private def updateBaseRouteDoc(context: BaseRouteAnalysisContext): Unit = {
      val baseRouteDoc = baseRouteDocBuilder.build(context)
      routeRepository.saveBaseRoute(baseRouteDoc)
    }

    private def updateRouteTileDocs(context: BaseRouteAnalysisContext): Unit = {
      val routeTileDocs = RouteTileDocBuilder.build(context)
      routeTileDocs.foreach(routeRepository.saveRouteTile)
    }

    private def updateWatchedRoutes(context: BaseRouteAnalysisContext): Unit = {
      analysisContext.watched.routes.add(context.relation.id, context.elementIds)
    }
  }
}
