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
import kpn.server.repository.RouteRepository
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
  routeRepository: RouteRepository,
  implicit val analysisExecutionContext: ExecutionContext
) extends RouteChangeProcessor {

  private val log = Log(classOf[RouteChangeProcessorImpl])

  override def process(context: ChangeSetContext): ChangeSetChanges = {
    log.debugElapsed {
      val routeElementChanges = changeAnalyzer.analyze(context)
      val batchSize = 50
      val changedRouteIds = routeElementChanges.elementIds
      if (changedRouteIds.nonEmpty) {
        log.info(s"${changedRouteIds.size} route(s) impacted: ${changedRouteIds.mkString(", ")}")
      }
      val routeChanges = changedRouteIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (routeIds, index) =>
        processBatch(context, routeElementChanges, routeIds)
      }.toSeq
      ("", ChangeSetChanges(routeChanges = routeChanges))
    }
  }

  private def processBatch(context: ChangeSetContext, routeElementChanges: ElementChanges, routeIds: Seq[Long]): Seq[RouteChange] = {
    val routeChangeDatas = readBeforeAndAfter(context, routeIds)
    routeChangeDatas.flatMap { routeChangeData =>
      val action = routeElementChanges.action(routeChangeData.routeId)
      processChangeData(context, routeChangeData, action)
    }
  }


  private def readBeforeAndAfter(context: ChangeSetContext, routeIds: Seq[Long]): Seq[RouteChangeData] = {
    val beforeRelations = overpassRepository.fullRelations(context.timestampBefore, routeIds)
    val afterRelations = overpassRepository.fullRelations(context.timestampAfter, routeIds)
    routeIds.map { routeId =>
      RouteChangeData(
        routeId,
        beforeRelations.find(_.id == routeId),
        afterRelations.find(_.id == routeId)
      )
    }
  }

  private def processChangeData(context: ChangeSetContext, data: RouteChangeData, action: ChangeAction): Option[RouteChange] = {

    val before = data.before.flatMap(masterRouteAnalyzer.analyze)
    val after = data.after.flatMap(masterRouteAnalyzer.analyze)

    if (action == ChangeAction.Create) {
      after.map { routeAnalysisAfter =>

        routeRepository.save(routeAnalysisAfter.route)
        analysisContext.data.routes.watched.add(data.routeId, routeAnalysisAfter.route.elementIds)

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

            routeRepository.save(afterRouteAnalysis.route)
            analysisContext.data.routes.watched.add(data.routeId, afterRouteAnalysis.route.elementIds)

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

          val routeInfo = routeAnalysisBefore.route.copy(
            labels = routeAnalysisBefore.route.labels.filterNot(_ == "active"),
            active = false
          )

          routeRepository.save(routeInfo)
          analysisContext.data.routes.watched.delete(data.routeId)

          tileChangeAnalyzer.analyzeRoute(routeAnalysisBefore)

          val key = context.buildChangeKey(data.routeId)

          Some(
            RouteChangeStateAnalyzer.analyzed(
              RouteChange(
                _id = key.toId,
                key = key,
                changeType = ChangeType.Delete,
                name = routeAnalysisBefore.route.summary.name,
                locationAnalysis = routeAnalysisBefore.route.analysis.locationAnalysis,
                addedToNetwork = Seq.empty,
                removedFromNetwork = Seq.empty,
                before = Some(routeAnalysisBefore.toRouteData),
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
    else {
      None
    }
  }

}
