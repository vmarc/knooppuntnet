package kpn.server.analyzer.engine.changes.route

import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Fact
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.route.create.RouteCreateProcessor
import kpn.server.analyzer.engine.changes.route.delete.RouteDeleteProcessor
import kpn.server.analyzer.engine.changes.route.update.RouteUpdateProcessor
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.TileChangeAnalyzer
import kpn.server.overpass.OverpassRepository
import org.springframework.stereotype.Component

import scala.concurrent.ExecutionContext

@Component
class RouteChangeProcessorImpl(
  analysisContext: AnalysisContext,
  changeAnalyzer: RouteChangeAnalyzer,
  createProcessor: RouteCreateProcessor,
  updateProcessor: RouteUpdateProcessor,
  deleteProcessor: RouteDeleteProcessor,
  overpassRepository: OverpassRepository,
  masterRouteAnalyzer: MasterRouteAnalyzer,
  tileChangeAnalyzer: TileChangeAnalyzer,
  implicit val analysisExecutionContext: ExecutionContext
) extends RouteChangeProcessor {

  private val log = Log(classOf[RouteChangeProcessorImpl])

  override def process(context: ChangeSetContext): ChangeSetChanges = {
    log.debugElapsed {
      val routeElementChanges = changeAnalyzer.analyze(context.changeSet)
      val batchSize = 50
      val allRouteIds = routeElementChanges.elementIds
      val routeChanges = allRouteIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (routeIds, index) =>
        processBatch(context, routeElementChanges, routeIds)
      }.toSeq
      ("", ChangeSetChanges(routeChanges = routeChanges))
    }
  }

  private def processBatch(context: ChangeSetContext, routeElementChanges: ElementChanges, routeIds: Seq[Long]): Seq[RouteChange] = {
    val beforeRelations = overpassRepository.fullRelations(context.timestampBefore, routeIds)
    val afterRelations = overpassRepository.fullRelations(context.timestampAfter, routeIds)
    val routeChangeDatas = routeIds.map { routeId =>
      RouteChangeData(
        routeId,
        beforeRelations.find(_.id == routeId),
        afterRelations.find(_.id == routeId)
      )
    }
    routeChangeDatas.flatMap { routeChangeData =>
      val action = routeElementChanges.action(routeChangeData.routeId)
      processXxx(context, routeChangeData, action)
    }
  }

  private def processXxx(context: ChangeSetContext, data: RouteChangeData, action: ChangeAction): Option[RouteChange] = {

    val before = data.before.flatMap(masterRouteAnalyzer.analyze)
    val after = data.before.flatMap(masterRouteAnalyzer.analyze)

    if (action == ChangeAction.Create) {
      after.map { routeAnalysisAfter =>
        val factDiffs = if (routeAnalysisAfter.route.facts.nonEmpty) {
          Some(
            FactDiffs(
              introduced = routeAnalysisAfter.route.facts.toSet
            )
          )
        }
        else {
          None
        }

        val key = context.buildChangeKey(routeAnalysisAfter.id)

        RouteChangeStateAnalyzer.analyzed(
          RouteChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Create,
            name = routeAnalysisAfter.name,
            locationAnalysis = routeAnalysisAfter.route.analysis.locationAnalysis,
            addedToNetwork = Seq.empty,
            removedFromNetwork = Seq.empty,
            before = None,
            after = Some(routeAnalysisAfter.toRouteData),
            removedWays = Seq.empty,
            addedWays = Seq.empty,
            updatedWays = Seq.empty,
            diffs = RouteDiff(
              factDiffs = factDiffs
            ),
            facts = Seq(Fact.OrphanRoute)
          )
        )
      }
    }
    else if (action == ChangeAction.Modify) {
      after.flatMap { afterRouteAnalysis =>
        before match {
          case None =>
            //noinspection SideEffectsInMonadicTransformation
            log.warn(s"Unexpected: 'before' analysis for route ${afterRouteAnalysis.route.id} not found")
            None

          case Some(beforeRouteAnalysis) =>

            tileChangeAnalyzer.analyzeRouteChange(beforeRouteAnalysis, afterRouteAnalysis)

            val routeUpdate = new RouteDiffAnalyzer(beforeRouteAnalysis, afterRouteAnalysis).analysis

            if (routeUpdate.facts.contains(Fact.LostRouteTags)) {
              analysisContext.data.routes.watched.delete(routeUpdate.id)
            }

            val facts = if (routeUpdate.facts.contains(Fact.LostRouteTags)) {
              Seq(Fact.WasOrphan) ++ routeUpdate.facts
            }
            else {
              Seq(Fact.OrphanRoute) ++ routeUpdate.facts
            }

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
                  facts = facts
                )
              )
            )
        }
      }
    }
    else if (action == ChangeAction.Delete) {
      analysisContext.data.routes.watched.delete(data.routeId)
      before match {
        case None =>
          //noinspection SideEffectsInMonadicTransformation
          log.warn(s"Unexpected: 'before' data for route ${data.routeId} at ${context.timestampBefore.yyyymmddhhmmss} not found, continue processing without reporting change")
          None

        case Some(routeAnalysisBefore) =>

          //  val route = routeAnalysisBefore.route.copy(/*orphan = true, */ active = false)
          //  routeRepository.save(route)
          //  routeRepository.saveElements(
          //    RouteElements(
          //      loadedRoute.id,
          //      loadedRoute.id,
          //      relationAnalyzer.toElementIds(routeAnalysis.relation)
          //    )
          //  )
          //  tileChangeAnalyzer.analyzeRoute(beforeRouteAnalysis)

          val key = context.buildChangeKey(data.routeId)

          //Some(
          //  RouteChangeStateAnalyzer.analyzed(
          //    RouteChange(
          //      _id = key.toId,
          //      key = key,
          //      changeType = ChangeType.Delete,
          //      name = route.summary.name,
          //      locationAnalysis = routeAnalysis.route.analysis.locationAnalysis,
          //      addedToNetwork = Seq.empty,
          //      removedFromNetwork = Seq.empty,
          //      before = Some(routeAnalysis.toRouteData),
          //      after = None,
          //      removedWays = Seq.empty,
          //      addedWays = Seq.empty,
          //      updatedWays = Seq.empty,
          //      diffs = RouteDiff(),
          //      facts = Seq(Fact.WasOrphan, Fact.Deleted)
          //    )
          //  )
          //)
          None
      }
    }
    else {
      None
    }
  }

}
