package kpn.server.analyzer.engine.changes.route.main

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteChangeProcessor(
  analysisContext: AnalysisContext,
  routeMainAnalyzer: RouteMainAnalyzer,
  routeRepository: RouteRepository,
  createProcessor: RouteChangeCreateProcessor,
  updateProcessor: RouteChangeUpdateProcessor,
  deleteProcessor: RouteChangeDeleteProcessor,
) extends ChangeProcessor {

  private val log = Log(classOf[RouteChangeProcessor])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {

      // TODO add context.impactedRouteIds to queue of impactedRouteIds
      // process queue + add to processed collection aftwards

      val routeChangeContexts = context.impactedRouteIds.flatMap { routeId =>
        processRoute(context, routeId)
      }

      val impactedNodeIds = (context.impactedNodeIds ++ routeChangeContexts.flatMap(_.impactedNodeIds)).distinct.sorted
      val impactedNetworkIds = (context.impactedNetworkIds ++ routeChangeContexts.flatMap(_.impactedNetworkIds)).distinct.sorted

      (
        s"${routeChangeContexts.size} route changes",
        context.copy(
          changes = context.changes.copy(
            routeChanges = routeChangeContexts.map(_.routeChange),
          ),
          impactedNodeIds = impactedNodeIds,
          impactedNetworkIds = impactedNetworkIds,
        )
      )
    }
  }

  private def processRoute(context: ChangeSetContext, routeId: Long): Option[RouteChangeContext] = {
    routeRepository.findRouteById(routeId) match {
      case None =>
        routeRepository.findBaseRouteById(routeId) match {
          case None =>
            // TODO nothing to do - log message?
            // TODO add parent route ids to impactedRouteIds queue
            None
          case Some(baseRouteDoc) =>
            if (baseRouteDoc.active) {
              routeMainAnalyzer.analyze(baseRouteDoc) match {
                case None =>
                  // TODO delete?
                  None

                case Some(routeDocAfter) =>
                  // CREATE
                  routeRepository.saveRoute(routeDocAfter)
                  createProcessor.process(context, routeDocAfter, routeId)
              }
            }
            else {
              // TODO add parent route ids to impactedRouteIds queue?
              // DELETE - nothing to do anymore?
              None
            }
        }

      case Some(before) =>
        // UPDATE
        routeRepository.findBaseRouteById(routeId) match {
          case None =>
            // TODO message?
            None
          case Some(baseRouteDoc) =>
            routeMainAnalyzer.analyze(baseRouteDoc) match {
              case None =>
                // TODO message? delete?
                None
              case Some(routeDoc) =>

                val parentRouteIds = routeDoc.parentRoutes.map(_.routeId)
                // TODO add to impactedRouteIds so that a parent route that contains a sub route that is changed will reprocessed also!

                if (context.baseRouteDeletedIds.contains(routeId)) {
                  routeRepository.saveRoute(routeDoc.deactivated)
                  deleteProcessor.process(context, before)
                }
                else {
                  routeRepository.saveRoute(routeDoc)
                  updateProcessor.process(context, before, routeDoc, routeId)
                }
            }
        }
    }
  }
}
