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
) {

  private val log = Log(classOf[BaseRouteChangeDeleteProcessor])

  def process(changeSetContext: ChangeSetContext, routeIds: Seq[Long]): ChangeSetContext = {
    val impacts = routeIds.flatMap(processRoute)
    changeSetContext.copy(
      baseRouteDeletedIds = routeIds
    ).withImpact(
      tiles = impacts.flatMap(_.impactedTiles),
      nodeIds = impacts.flatMap(_.impactedNodeIds),
      routeIds = routeIds,
    )
  }

  private def processRoute(routeId: Long): Option[ChangeImpact] = {
    analysisContext.watched.routes.delete(routeId)
    routeRepository.findBaseRouteById(routeId) match {
      case Some(baseRouteDoc) =>
        routeRepository.saveBaseRoute(baseRouteDoc.deactivated)
        Some(
          ChangeImpact(
            baseRouteDoc.nodes.nodeIds,
            baseRouteDoc.tiles
          )
        )
      case None =>
        // TODO report?
        None
    }
  }
}
