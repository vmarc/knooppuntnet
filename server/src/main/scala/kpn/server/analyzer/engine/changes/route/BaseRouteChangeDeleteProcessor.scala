package kpn.server.analyzer.engine.changes.route

import kpn.core.doc.BaseRouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeDeleteProcessor(
  analysisContext: AnalysisContext,
  routeRepository: RouteRepository,
) extends BaseRouteChangeSubProcessor {

  private val defaultLog = Log(classOf[BaseRouteChangeDeleteProcessor])

  def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    loggedProcess(changeSetContext, routeId, defaultLog): ChangeSetContext
  }

  def loggedProcess(changeSetContext: ChangeSetContext, routeId: Long, log: Log): ChangeSetContext = {
    analysisContext.watched.routes.delete(routeId)
    routeRepository.deleteRouteTiles(routeId)
    routeRepository.findBaseRouteById(routeId) match {
      case Some(baseRouteDoc) =>
        deactivateRoute(changeSetContext, routeId, baseRouteDoc)
      case None =>
        handleMissingRoute(changeSetContext, routeId, log)
    }
  }

  private def deactivateRoute(changeSetContext: ChangeSetContext, routeId: Long, baseRouteDoc: BaseRouteDoc): ChangeSetContext = {
    routeRepository.saveBaseRoute(baseRouteDoc.deactivated)
    changeSetContext.withImpact(
      tiles = baseRouteDoc.tiles,
      nodeIds = baseRouteDoc.nodes.nodeIds,
      routeIds = Seq(routeId)
    )
  }

  private def handleMissingRoute(changeSetContext: ChangeSetContext, routeId: Long, log: Log): ChangeSetContext = {
    log.warn(s"route $routeId not found")
    changeSetContext.withImpact(
      routeIds = Seq(routeId)
    )
  }
}
