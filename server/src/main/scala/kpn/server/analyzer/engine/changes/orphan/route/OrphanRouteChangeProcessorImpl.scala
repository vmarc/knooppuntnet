package kpn.server.analyzer.engine.changes.orphan.route

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Fact
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.route.RouteChangeStateAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.TileChangeAnalyzer
import kpn.server.analyzer.load.RoutesLoader
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class OrphanRouteChangeProcessorImpl(
  analysisContext: AnalysisContext,
  orphanRouteChangeAnalyzer: OrphanRouteChangeAnalyzer,
  orphanRouteProcessor: OrphanRouteProcessor,
  routesLoader: RoutesLoader,
  routeAnalyzer: MasterRouteAnalyzer,
  routeRepository: RouteRepository,
  tileChangeAnalyzer: TileChangeAnalyzer
) extends OrphanRouteChangeProcessor {

  private val log = Log(classOf[OrphanRouteChangeProcessorImpl])

  def process(context: ChangeSetContext): ChangeSetChanges = {
    log.debugElapsed {

      val changes = orphanRouteChangeAnalyzer.analyze(context.changeSet)

      val creates = processCreates(context, changes.creates)
      val updates = processUpdates(context, changes.updates)
      val deletes = processDeletes(context, changes.deletes)

      // TODO can/should also update context.nodeChanges here ? YES

      val routeChanges = creates ++ updates ++ deletes

      val message = s"creates=${creates.size}, updates=${updates.size}, deletes=${deletes.size}"
      (message, ChangeSetChanges(routeChanges = routeChanges))
    }
  }

  private def processCreates(context: ChangeSetContext, routeIds: Seq[Long]): Seq[RouteChange] = {

    val loadedRoutes = routesLoader.load(context.changeSet.timestampAfter, routeIds).flatten
    val routeAnalyses = loadedRoutes.flatMap(loadedRoute => orphanRouteProcessor.process(context, loadedRoute))
    routeAnalyses.foreach(tileChangeAnalyzer.analyzeRoute)

    routeAnalyses.map { routeAnalysis =>

      val factDiffs = if (routeAnalysis.route.facts.nonEmpty) {
        Some(
          FactDiffs(
            introduced = routeAnalysis.route.facts.toSet
          )
        )
      }
      else {
        None
      }

      throw new IllegalStateException("Unexpected code execution - would need to calculate impactedNodeIds here")
      val impactedNodeIds: Seq[Long] = Seq.empty

      val key = context.buildChangeKey(routeAnalysis.id)

      RouteChangeStateAnalyzer.analyzed(
        RouteChange(
          _id = key.toId,
          key = key,
          changeType = ChangeType.Create,
          name = routeAnalysis.name,
          locationAnalysis = routeAnalysis.route.analysis.locationAnalysis,
          addedToNetwork = Seq.empty,
          removedFromNetwork = Seq.empty,
          before = None,
          after = Some(routeAnalysis.toRouteData),
          removedWays = Seq.empty,
          addedWays = Seq.empty,
          updatedWays = Seq.empty,
          diffs = RouteDiff(
            factDiffs = factDiffs
          ),
          facts = Seq(Fact.OrphanRoute),
          impactedNodeIds
        )
      )
    }
  }

  private def processUpdates(context: ChangeSetContext, routeIds: Seq[Long]): Seq[RouteChange] = {

    val afterLoadedRoutes = routesLoader.load(context.changeSet.timestampAfter, routeIds).flatten
    val afterRouteAnalyses = afterLoadedRoutes.flatMap(loadedRoute => orphanRouteProcessor.process(context, loadedRoute))

    val updatedRouteIds = afterRouteAnalyses.map(_.route.id) // note: this excludes ignored routes
    val beforeLoadedRoutes = routesLoader.load(context.changeSet.timestampBefore, updatedRouteIds).flatten
    val beforeRouteAnalyses = beforeLoadedRoutes.flatMap { loadedRoute =>
      routeAnalyzer.analyze(loadedRoute.relation /*, orphan = true*/)
    }

    afterRouteAnalyses.flatMap { afterRouteAnalysis =>
      beforeRouteAnalyses.find(_.route.id == afterRouteAnalysis.route.id) match {
        case None =>
          //noinspection SideEffectsInMonadicTransformation
          log.warn(s"Unexpected: 'before' analysis for route ${afterRouteAnalysis.route.id} not found")
          None

        case Some(beforeRouteAnalysis) =>

          tileChangeAnalyzer.analyzeRouteChange(beforeRouteAnalysis, afterRouteAnalysis)

          val routeUpdate = new RouteDiffAnalyzer(beforeRouteAnalysis, afterRouteAnalysis).analysis

          if (routeUpdate.facts.contains(Fact.LostRouteTags)) {
            analysisContext.watched.routes.delete(routeUpdate.id)
          }

          val facts = if (routeUpdate.facts.contains(Fact.LostRouteTags)) {
            Seq(Fact.WasOrphan) ++ routeUpdate.facts
          }
          else {
            Seq(Fact.OrphanRoute) ++ routeUpdate.facts
          }

          throw new IllegalStateException("Unexpected code execution - would need to calculate impactedNodeIds here")
          val impactedNodeIds: Seq[Long] = Seq.empty

          val key = context.buildChangeKey(routeUpdate.after.id)

          Some(
            RouteChangeStateAnalyzer.analyzed(
              RouteChange(
                _id = key.toId,
                key = key,
                changeType = ChangeType.Update,
                name = routeUpdate.after.name,
                locationAnalysis = afterRouteAnalysis.route.analysis.locationAnalysis,
                addedToNetwork = Seq.empty,
                removedFromNetwork = Seq.empty,
                before = Some(routeUpdate.before.toRouteData),
                after = Some(routeUpdate.after.toRouteData),
                removedWays = routeUpdate.removedWays,
                addedWays = routeUpdate.addedWays,
                updatedWays = routeUpdate.updatedWays,
                diffs = routeUpdate.diffs,
                facts = facts,
                impactedNodeIds
              )
            )
          )
      }
    }
  }

  private def processDeletes(context: ChangeSetContext, routeIds: Seq[Long]): Seq[RouteChange] = {

    val loadedRoutes = routesLoader.load(context.timestampBefore, routeIds).flatten

    routeIds.flatMap { routeId =>

      analysisContext.watched.routes.delete(routeId)

      loadedRoutes.find(_.id == routeId) match {
        case None =>
          //noinspection SideEffectsInMonadicTransformation
          log.warn(s"Unexpected: 'before' data for route $routeId at ${context.timestampBefore.yyyymmddhhmmss} not found, continue processing without reporting change")
          None

        case Some(loadedRoute) =>

          val routeAnalysis = routeAnalyzer.analyze(loadedRoute.relation /*, orphan = true*/).get

          val route = routeAnalysis.route.copy(/*orphan = true, */ active = false)
          routeRepository.save(route)
          tileChangeAnalyzer.analyzeRoute(routeAnalysis)

          throw new IllegalStateException("Unexpected code execution - would need to calculate impactedNodeIds here")
          val impactedNodeIds: Seq[Long] = Seq.empty

          val key = context.buildChangeKey(route.id)

          Some(
            RouteChangeStateAnalyzer.analyzed(
              RouteChange(
                _id = key.toId,
                key = key,
                changeType = ChangeType.Delete,
                name = route.summary.name,
                locationAnalysis = routeAnalysis.route.analysis.locationAnalysis,
                addedToNetwork = Seq.empty,
                removedFromNetwork = Seq.empty,
                before = Some(routeAnalysis.toRouteData),
                after = None,
                removedWays = Seq.empty,
                addedWays = Seq.empty,
                updatedWays = Seq.empty,
                diffs = RouteDiff(),
                facts = Seq(Fact.WasOrphan, Fact.Deleted),
                impactedNodeIds
              )
            )
          )
      }
    }
  }
}
