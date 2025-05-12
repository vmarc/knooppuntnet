package kpn.server.analyzer.engine.changes.route

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
class BaseRouteChangeCreateProcessor(
  analysisContext: AnalysisContext,
  rawDataRepository: RawDataRepository,
  routeRepository: RouteRepository,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  baseRouteDocBuilder: BaseRouteDocBuilder
) extends BaseRouteChangeSubProcessor {

  private val defaultLog = Log(classOf[BaseRouteChangeCreateProcessor])

  def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    loggedProcess(changeSetContext, routeId, defaultLog)
  }

  def loggedProcess(changeSetContext: ChangeSetContext, routeId: Long, log: Log): ChangeSetContext = {
    rawDataRepository.route(changeSetContext.changeSet.timestampAfter, routeId) match {
      case Some(rawRouteDoc) =>
        processRoute(changeSetContext, rawRouteDoc, log)
      case None =>
        handleRouteNotFound(changeSetContext, routeId, log)
    }
  }

  private def processRoute(changeSetContext: ChangeSetContext, rawRouteDoc: RawRouteDoc, log: Log): ChangeSetContext = {
    val context = baseRouteMainAnalyzer.analyze(rawRouteDoc.relation, rawRouteDoc.structure)
    if (context.abort) {
      handleAbortedRouteAnalysis(changeSetContext, context)
    }
    else {
      processRouteAnalysisResult(changeSetContext, context)
    }
  }

  private def processRouteAnalysisResult(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext): ChangeSetContext = {
    updateRouteData(context)
    changeSetContext.withImpact(
      tileIds = context.tiles,
      nodeIds = context.routeNodesAnalysis.nodeIds,
      routeIds = Seq(context.routeId),
    )
  }

  private def handleAbortedRouteAnalysis(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext): ChangeSetContext = {
    if (context.facts.contains(Fact.LostRouteTags)) {
      val baseRouteDoc = baseRouteDocBuilder.build(context)
      changeSetContext.withImpact(
        tileIds = baseRouteDoc.tiles,
        nodeIds = baseRouteDoc.nodes.nodeIds,
        routeIds = Seq(baseRouteDoc._id),
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

  private def handleRouteNotFound(changeSetContext: ChangeSetContext, routeId: Long, log: Log): ChangeSetContext = {
    log.warn(s"overpass route $routeId not found")
    changeSetContext.withImpact(
      routeIds = Seq(routeId),
    )
  }
}
