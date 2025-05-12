package kpn.server.analyzer.engine.changes.route

import kpn.core.doc.BaseRouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeDeleter(
  analysisContext: AnalysisContext,
  routeRepository: RouteRepository,
) {

  private val defaultLog = Log(classOf[BaseRouteChangeDeleter])

  def delete(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    loggedDelete(changeSetContext, routeId, defaultLog)
  }

  def loggedDelete(changeSetContext: ChangeSetContext, routeId: Long, log: Log): ChangeSetContext = {
    stopWatching(routeId)
    val context1 = deleteRouteTileDocs(changeSetContext, routeId)
    val context2 = deleteBaseRouteDoc(context1, routeId, log)
    context2.withImpact(routeIds = Seq(routeId))
  }

  private def deleteBaseRouteDoc(changeSetContext: ChangeSetContext, routeId: Long, log: Log): ChangeSetContext = {
    routeRepository.findBaseRouteById(routeId) match {
      case Some(baseRouteDoc) =>
        deactivateBaseRouteDoc(changeSetContext, baseRouteDoc)
      case None =>
        log.warn(s"route $routeId not found")
        changeSetContext
    }
  }

  private def deactivateBaseRouteDoc(changeSetContext: ChangeSetContext, baseRouteDoc: BaseRouteDoc): ChangeSetContext = {
    routeRepository.saveBaseRoute(baseRouteDoc.deactivated)
    changeSetContext.withImpact(
      nodeIds = baseRouteDoc.nodes.nodeIds
    )
  }

  private def stopWatching(routeId: Long): Unit = {
    analysisContext.watched.routes.delete(routeId)
  }

  private def deleteRouteTileDocs(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    val tileIds = routeRepository.routeTileIds(routeId)
    tileIds.foreach(routeRepository.deleteRouteTile)
    changeSetContext.withImpact(tileIds = tileIds)
  }
}
