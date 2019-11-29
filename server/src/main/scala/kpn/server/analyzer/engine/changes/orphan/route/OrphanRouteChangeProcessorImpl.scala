package kpn.server.analyzer.engine.changes.orphan.route

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Fact
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.NetworkNodeBuilder
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.route.RouteChangeAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.TileChangeAnalyzer
import kpn.server.analyzer.load.RoutesLoader
import kpn.server.repository.AnalysisRepository
import org.springframework.stereotype.Component

@Component
class OrphanRouteChangeProcessorImpl(
  analysisContext: AnalysisContext,
  analysisRepository: AnalysisRepository,
  orphanRouteChangeAnalyzer: OrphanRouteChangeAnalyzer,
  orphanRouteProcessor: OrphanRouteProcessor,
  routesLoader: RoutesLoader,
  routeAnalyzer: MasterRouteAnalyzer,
  countryAnalyzer: CountryAnalyzer,
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

    val creates = routeAnalyses.map(_.toRouteData)

    creates.map { routeData =>
      analyzed(
        RouteChange(
          key = context.buildChangeKey(routeData.id),
          changeType = ChangeType.Create,
          name = routeData.name,
          addedToNetwork = Seq.empty,
          removedFromNetwork = Seq.empty,
          before = None,
          after = Some(routeData),
          removedWays = Seq.empty,
          addedWays = Seq.empty,
          updatedWays = Seq.empty,
          diffs = RouteDiff(),
          facts = Seq(Fact.OrphanRoute)
        )
      )
    }
  }

  private def processUpdates(context: ChangeSetContext, routeIds: Seq[Long]): Seq[RouteChange] = {

    val afterLoadedRoutes = routesLoader.load(context.changeSet.timestampAfter, routeIds).flatten
    val afterRouteAnalyses = afterLoadedRoutes.flatMap(loadedRoute => orphanRouteProcessor.process(context, loadedRoute))

    val updatedRouteIds = afterRouteAnalyses.map(_.route.id) // note: this excludes ignored routes
    val beforeLoadedRoutes = routesLoader.load(context.changeSet.timestampBefore, updatedRouteIds).flatten
    val beforeRouteAnalyses = beforeLoadedRoutes.map { loadedRoute =>
      val allNodes = new NetworkNodeBuilder(analysisContext, loadedRoute.data, loadedRoute.networkType, countryAnalyzer).networkNodes
      routeAnalyzer.analyze(allNodes, loadedRoute, orphan = true)
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
            analysisContext.data.orphanRoutes.watched.delete(routeUpdate.id)
          }

          val facts = if (routeUpdate.facts.contains(Fact.LostRouteTags)) {
            Seq(Fact.WasOrphan) ++ routeUpdate.facts
          }
          else {
            Seq(Fact.OrphanRoute) ++ routeUpdate.facts
          }

          Some(
            analyzed(
              RouteChange(
                key = context.buildChangeKey(routeUpdate.after.id),
                changeType = ChangeType.Update,
                name = routeUpdate.after.name,
                addedToNetwork = Seq.empty,
                removedFromNetwork = Seq.empty,
                before = Some(routeUpdate.before.toRouteData),
                after = Some(routeUpdate.after.toRouteData),
                removedWays = routeUpdate.removedWays,
                addedWays = routeUpdate.addedWays,
                updatedWays = routeUpdate.updatedWays,
                diffs = routeUpdate.diffs,
                facts = facts
              )
            )
          )
      }
    }
  }

  private def processDeletes(context: ChangeSetContext, routeIds: Seq[Long]): Seq[RouteChange] = {

    val loadedRoutes = routesLoader.load(context.timestampBefore, routeIds).flatten

    routeIds.flatMap { routeId =>

      analysisContext.data.orphanRoutes.watched.delete(routeId)

      loadedRoutes.find(_.id == routeId) match {
        case None =>
          //noinspection SideEffectsInMonadicTransformation
          log.warn(s"Unexpected: 'before' data for route $routeId at ${context.timestampBefore.yyyymmddhhmmss} not found, continue processing without reporting change")
          None

        case Some(loadedRoute) =>

          val allNodes = new NetworkNodeBuilder(analysisContext, loadedRoute.data, loadedRoute.networkType, countryAnalyzer).networkNodes
          val routeAnalysis = routeAnalyzer.analyze(allNodes, loadedRoute, orphan = true)

          val route = routeAnalysis.route.copy(orphan = true, active = false)
          analysisRepository.saveRoute(route)
          tileChangeAnalyzer.analyzeRoute(routeAnalysis)

          Some(
            analyzed(
              RouteChange(
                key = context.buildChangeKey(route.id),
                changeType = ChangeType.Delete,
                name = route.summary.name,
                addedToNetwork = Seq.empty,
                removedFromNetwork = Seq.empty,
                before = Some(routeAnalysis.toRouteData),
                after = None,
                removedWays = Seq.empty,
                addedWays = Seq.empty,
                updatedWays = Seq.empty,
                diffs = RouteDiff(),
                facts = Seq(Fact.WasOrphan, Fact.Deleted)
              )
            )
          )
      }
    }
  }

  private def analyzed(routeChange: RouteChange): RouteChange = {
    new RouteChangeAnalyzer(routeChange).analyzed()
  }

}
