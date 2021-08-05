package kpn.server.analyzer.engine.changes.route

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

      val routeChanges = changeAnalyzer.analyze(context.changeSet)

      val batchSize = 50
      val allRouteIds = routeChanges.elementIds
      val xx = allRouteIds.sliding(batchSize, batchSize).zipWithIndex.map { case (routeIds, index) =>
        val beforeRelations = overpassRepository.fullRelations(context.timestampBefore, routeIds)
        val afterRelations = overpassRepository.fullRelations(context.timestampAfter, routeIds)
        val routeChangeDatas = routeIds.map { routeId =>
          RouteChangeData(
            routeId,
            beforeRelations.find(_.id == routeId),
            afterRelations.find(_.id == routeId)
          )
        }

        val createRouteIds = routeChanges.creates.filter(routeIds.contains)
        val updateRouteIds = routeChanges.updates.filter(routeIds.contains)
        val deleteRouteIds = routeChanges.deletes.filter(routeIds.contains)

        val createRouteChangeDatas = createRouteIds.flatMap(id => routeChangeDatas.find(_.routeId == id))
        val updateRouteChangeDatas = updateRouteIds.flatMap(id => routeChangeDatas.find(_.routeId == id))
        val deleteRouteChangeDatas = deleteRouteIds.flatMap(id => routeChangeDatas.find(_.routeId == id))

        val xx = createRouteChangeDatas.map(d => processCreate(context, d))
        val yy = updateRouteChangeDatas.map(d => processUpdate(context, d))
        val zz = deleteRouteChangeDatas.map(d => processDelete(context, d))

        val aa = xx ++ yy ++ zz
      }
      //      val createFutures = routeChanges.creates.map(id => createProcessor.process(context, id))
      //      val updateFutures = routeChanges.updates.map(id => updateProcessor.process(context, id))
      //      val deleteFutures = routeChanges.deletes.map(id => deleteProcessor.process(context, id))
      //
      //      val futures = createFutures ++ updateFutures ++ deleteFutures
      //      val future = Future.sequence(futures)
      //      val changes = Await.result(future, Duration(10, TimeUnit.MINUTES))
      //
      //
      //      val changeSetChanges = merge(changes: _*)
      //      val message = s"actions=${routeChanges.actionCount}, creates=${createFutures.size}, updates=${updateFutures.size}, deletes=${deleteFutures.size}"
      //      (message, changeSetChanges)
      //    }
      ("", ChangeSetChanges())
    }
  }

  private def processCreate(context: ChangeSetContext, data: RouteChangeData): ChangeSetChanges = {

    val before = data.before.flatMap(masterRouteAnalyzer.analyze)
    val after = data.before.flatMap(masterRouteAnalyzer.analyze)

    val routeChangeOption = after.map { routeAnalysis =>

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
          facts = Seq(Fact.OrphanRoute)
        )
      )
    }

    ChangeSetChanges(routeChanges = routeChangeOption.toSeq)
  }

  private def processUpdate(context: ChangeSetContext, data: RouteChangeData): ChangeSetChanges = {
    val before = data.before.flatMap(masterRouteAnalyzer.analyze)
    val after = data.before.flatMap(masterRouteAnalyzer.analyze)

    val routeChangeOption = after.flatMap { afterRouteAnalysis =>
      before match {
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
    ChangeSetChanges(routeChanges = routeChangeOption.toSeq)
  }

  private def processDelete(context: ChangeSetContext, data: RouteChangeData): ChangeSetChanges = {
    val before = data.before.flatMap(masterRouteAnalyzer.analyze)
    val after = data.before.flatMap(masterRouteAnalyzer.analyze)

    //    analysisContext.data.orphanRoutes.watched.delete(data.routeId)
    //
    //    val routeChangeOption = before.map {
    //      case None =>
    //        //noinspection SideEffectsInMonadicTransformation
    //        log.warn(s"Unexpected: 'before' data for route ${data.routeId} at ${context.timestampBefore.yyyymmddhhmmss} not found, continue processing without reporting change")
    //        None
    //
    //      case Some(beforeRouteAnalysis) =>
    //
    //          val route = beforeRouteAnalysis.route.copy(/*orphan = true, */ active = false)
    //          routeRepository.save(route)
    //          routeRepository.saveElements(
    //            RouteElements(
    //              loadedRoute.id,
    //              loadedRoute.id,
    //              relationAnalyzer.toElementIds(routeAnalysis.relation)
    //            )
    //          )
    //          tileChangeAnalyzer.analyzeRoute(beforeRouteAnalysis)
    //
    //          val key = context.buildChangeKey(data.routeId)
    //
    //          Some(
    //            RouteChangeStateAnalyzer.analyzed(
    //              RouteChange(
    //                _id = key.toId,
    //                key = key,
    //                changeType = ChangeType.Delete,
    //                name = route.summary.name,
    //                locationAnalysis = routeAnalysis.route.analysis.locationAnalysis,
    //                addedToNetwork = Seq.empty,
    //                removedFromNetwork = Seq.empty,
    //                before = Some(routeAnalysis.toRouteData),
    //                after = None,
    //                removedWays = Seq.empty,
    //                addedWays = Seq.empty,
    //                updatedWays = Seq.empty,
    //                diffs = RouteDiff(),
    //                facts = Seq(Fact.WasOrphan, Fact.Deleted)
    //              )
    //            )
    //          )
    //      }
    //    }
    //    ChangeSetChanges(routeChanges = routeChangeOption.toSeq)

    ChangeSetChanges()
  }

}
