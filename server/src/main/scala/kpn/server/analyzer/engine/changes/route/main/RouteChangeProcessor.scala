package kpn.server.analyzer.engine.changes.route.main

import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.RouteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class RouteChangeProcessor(
  routeMainAnalyzer: RouteMainAnalyzer,
  routeRepository: RouteRepository,
  createProcessor: RouteChangeCreateProcessor,
  updateProcessor: RouteChangeUpdateProcessor,
  deleteProcessor: RouteChangeDeleteProcessor,
  @Autowired(required = false)
  log: Log = Log(classOf[RouteChangeProcessor])
) extends ChangeProcessor {

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {
      val impactedRouteIds = initializeImpactedRouteIds(context)
      val routeChangeContexts = processRoutes(context, impactedRouteIds)
      val updatedContext = updateContextWithImpactedIds(context, routeChangeContexts)
      (s"${routeChangeContexts.size} route changes", updatedContext)
    }
  }

  private def initializeImpactedRouteIds(context: ChangeSetContext): ImpactedRouteIds = {
    val impactedRouteIds = new ImpactedRouteIds()
    context.impactedRouteIds.foreach(impactedRouteIds.add)
    impactedRouteIds
  }

  private def processRoutes(context: ChangeSetContext, impactedRouteIds: ImpactedRouteIds): Seq[RouteChangeContext] = {
    impactedRouteIds.flatMap { routeId =>
      processRoute(context, impactedRouteIds, routeId)
    }.toSeq
  }

  private def processRoute(context: ChangeSetContext, impactedRouteIds: ImpactedRouteIds, routeId: Long): Option[RouteChangeContext] = {
    routeRepository.findRouteById(routeId) match {
      case None =>
        processNewRoute(context, routeId)

      case Some(before) =>
        processExistingRoute(context, impactedRouteIds, routeId, before)
    }
  }

  private def processNewRoute(context: ChangeSetContext, routeId: Long): Option[RouteChangeContext] = {
    routeRepository.findBaseRouteById(routeId) match {
      case None =>
        log.warn(s"unexpected: BaseRouteDoc($routeId) missing while processing new route")
        None
      case Some(baseRouteDoc) =>
        if (baseRouteDoc.active) {
          routeMainAnalyzer.analyze(baseRouteDoc) match {
            case None =>
              log.warn(s"unexpected: BaseRouteDoc($routeId) is active, but new RouteDoc analysis failed")
              None

            case Some(routeDocAfter) =>
              routeRepository.saveRoute(routeDocAfter)
              createProcessor.process(context, routeDocAfter, routeId)
          }
        }
        else {
          None
        }
    }
  }

  private def processExistingRoute(
    context: ChangeSetContext,
    impactedRouteIds: ImpactedRouteIds,
    routeId: Long,
    before: RouteDoc
  ): Option[RouteChangeContext] = {
    routeRepository.findBaseRouteById(routeId) match {
      case Some(baseRouteDoc) =>
        analyzeExistingRoute(context, impactedRouteIds, routeId, before, baseRouteDoc)
      case None =>
        handleMissingBaseRoute(before)
    }
  }

  private def analyzeExistingRoute(
    context: ChangeSetContext,
    impactedRouteIds: ImpactedRouteIds,
    routeId: Long,
    before: RouteDoc,
    baseRouteDoc: BaseRouteDoc
  ): Option[RouteChangeContext] = {
    routeMainAnalyzer.analyze(baseRouteDoc) match {
      case None => handleExistingRouteFailedAnalysis(before)

      case Some(routeDoc) =>

        updateImpactedRouteIds(impactedRouteIds, routeDoc)

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

  private def handleExistingRouteFailedAnalysis(routeDoc: RouteDoc): Option[RouteChangeContext] = {
    log.warn(s"unexpected: RouteDoc(${routeDoc._id}) update analysis failed")
    routeRepository.saveRoute(routeDoc.deactivated)
    None
  }

  private def updateImpactedRouteIds(impactedRouteIds: ImpactedRouteIds, routeDoc: RouteDoc): Unit = {
    val parentRouteIds = routeDoc.parentRoutes.map(_.routeId)
    parentRouteIds.foreach(impactedRouteIds.add)
  }

  private def handleMissingBaseRoute(routeDoc: RouteDoc): Option[RouteChangeContext] = {
    log.warn(s"unexpected: BaseRouteDoc(${routeDoc._id}) not found while updating route")
    routeRepository.saveRoute(routeDoc.deactivated)
    None
  }

  private def updateContextWithImpactedIds(
    context: ChangeSetContext,
    routeChangeContexts: Seq[RouteChangeContext]
  ): ChangeSetContext = {

    val impactedNodeIds = mergeAndSortIds(context.impactedNodeIds, routeChangeContexts.flatMap(_.impactedNodeIds))
    val impactedNetworkIds = mergeAndSortIds(context.impactedNetworkIds, routeChangeContexts.flatMap(_.impactedNetworkIds))

    context.copy(
      changes = context.changes.copy(
        routeChanges = routeChangeContexts.map(_.routeChange)
      ),
      impactedNodeIds = impactedNodeIds,
      impactedNetworkIds = impactedNetworkIds
    )
  }

  private def mergeAndSortIds(existingIds: Seq[Long], newIds: Seq[Long]): Seq[Long] = {
    (existingIds ++ newIds).distinct.sorted
  }
}
