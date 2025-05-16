package kpn.server.analyzer.engine.changes.route.base

import kpn.core.doc.BaseRouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeDeleterImpl(
  analysisContext: AnalysisContext,
  routeRepository: RouteRepository,
) extends BaseRouteChangeDeleter {

  private val defaultLog = Log(classOf[BaseRouteChangeDeleterImpl])

  def delete(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    loggedDelete(defaultLog, changeSetContext, routeId)
  }

  def loggedDelete(log: Log, changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
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
