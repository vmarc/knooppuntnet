package kpn.server.analyzer.engine.changes.route.base

import kpn.core.doc.BaseRouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteTileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class BaseRouteChangeDeleterImpl(
  analysisContext: AnalysisContext,
  routeRepository: RouteRepository,
  routeTileRepository: RouteTileRepository,
  @Autowired(required = false)
  log: Log = Log(classOf[BaseRouteChangeDeleterImpl])
) extends BaseRouteChangeDeleter {

  def delete(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    unwatchRoute(routeId)
    val context1 = deleteRouteTiles(changeSetContext, routeId)
    val context2 = deleteBaseRouteDoc(context1, routeId)
    context2.withImpact(routeIds = Seq(routeId))
  }

  private def unwatchRoute(routeId: Long): Unit = {
    analysisContext.watched.routes.delete(routeId)
  }

  private def deleteRouteTiles(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    val tileIds = routeTileRepository.routeTileIds(routeId)
    tileIds.foreach(routeTileRepository.deleteRouteTile)
    changeSetContext.withImpact(tileIds = tileIds)
  }

  private def deleteBaseRouteDoc(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
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
      nodeIds = baseRouteDoc.base.nodes.nodeIds
    )
  }
}
