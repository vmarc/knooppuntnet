package kpn.server.analyzer.engine.changes.route

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

  private val log = Log(classOf[BaseRouteChangeDeleteProcessor])

  def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    analysisContext.watched.routes.delete(routeId)
    routeRepository.findBaseRouteById(routeId) match {
      case Some(baseRouteDoc) =>
        // TODO redesign - remove tile docs
        routeRepository.saveBaseRoute(baseRouteDoc.deactivated)
        changeSetContext.withImpact(
          tiles = baseRouteDoc.tiles,
          nodeIds = baseRouteDoc.nodes.nodeIds,
          routeIds = Seq(routeId)
        )
      case None =>
        // TODO report?
        changeSetContext
    }
  }
}
