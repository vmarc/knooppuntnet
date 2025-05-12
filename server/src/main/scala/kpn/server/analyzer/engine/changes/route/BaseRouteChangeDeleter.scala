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
    new Deleter(log, routeId).delete(changeSetContext)
  }

  private class Deleter(log: Log, routeId: Long) {

    def delete(changeSetContext: ChangeSetContext): ChangeSetContext = {
      unwatchRoute()
      val context1 = deleteRouteTileDocs(changeSetContext)
      val context2 = deleteBaseRouteDoc(context1)
      context2.withImpact(routeIds = Seq(routeId))
    }

    private def unwatchRoute(): Unit = {
      analysisContext.watched.routes.delete(routeId)
    }

    private def deleteRouteTileDocs(changeSetContext: ChangeSetContext): ChangeSetContext = {
      val tileIds = routeRepository.routeTileIds(routeId)
      tileIds.foreach(routeRepository.deleteRouteTile)
      changeSetContext.withImpact(tileIds = tileIds)
    }

    private def deleteBaseRouteDoc(changeSetContext: ChangeSetContext): ChangeSetContext = {
      routeRepository.findBaseRouteById(routeId) match {
        case Some(baseRouteDoc) => deactivateBaseRouteDoc(changeSetContext, baseRouteDoc)
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
  }
}
