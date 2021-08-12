package kpn.server.analyzer.engine.changes.route

import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Fact
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
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
  overpassRepository: OverpassRepository,
  masterRouteAnalyzer: MasterRouteAnalyzer,
  tileChangeAnalyzer: TileChangeAnalyzer,
  routeRepository: RouteRepository,
  implicit val analysisExecutionContext: ExecutionContext
) extends RouteChangeProcessor {

  private val log = Log(classOf[RouteChangeProcessorImpl])

  override def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {

      val impactedRelationIds = context.changes.newNetworkChanges.flatMap(_.impactedRelationIds).distinct.sorted
      val routeElementChanges = changeAnalyzer.analyze(context)
      val batchSize = 50
      val changedRouteIds = (routeElementChanges.elementIds ++ impactedRelationIds).distinct.sorted
      if (changedRouteIds.nonEmpty) {
        log.info(s"${changedRouteIds.size} route(s) impacted: ${changedRouteIds.mkString(", ")}")
      }
      val routeChanges = changedRouteIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (routeIds, index) =>
        processBatch(context, routeElementChanges, routeIds)
      }.toSeq

      (
        s"${routeChanges.size} route changes",
        context.copy(
          changes = context.changes.copy(
            routeChanges = routeChanges
          )
        )
      )
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

    val beforeOption = data.before.flatMap(masterRouteAnalyzer.analyze)
    val afterOption = data.after.flatMap(masterRouteAnalyzer.analyze)

    beforeOption match {
      case None =>
        afterOption match {
          case None => None // TODO message ?
          case Some(after) =>
            Some(processCreate(context, after, data.routeId))
        }
      case Some(before) =>
        afterOption match {
          case None => Some(processDelete(context, before, data.routeId))
          case Some(after) => Some(processUpdate(context, before, after, data.routeId))
        }
    }
  }

  private def processCreate(context: ChangeSetContext, after: RouteAnalysis, routeId: Long): RouteChange = {

    routeRepository.save(after.route)
    analysisContext.data.routes.watched.add(routeId, after.route.elementIds)

    val factDiffs = if (after.route.facts.nonEmpty) {
      Some(
        FactDiffs(
          introduced = after.route.facts.toSet
        )
      )
    }
    else {
      None
    }

    val impactedNodeIds: Seq[Long] = after.routeNodeAnalysis.routeNodes.map(_.node.id).distinct.sorted

    val key = context.buildChangeKey(routeId)

    val addedToNetwork = context.changes.newNetworkChanges.flatMap { networkChanges =>
      if (networkChanges.relations.added.contains(routeId)) {
        Some(networkChanges.toRef)
      }
      else {
        None
      }
    }

    val removedFromNetwork = context.changes.newNetworkChanges.flatMap { networkChanges =>
      if (networkChanges.relations.removed.contains(routeId)) {
        Some(networkChanges.toRef)
      }
      else {
        None
      }
    }

    RouteChangeStateAnalyzer.analyzed(
      RouteChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.Create,
        name = after.name,
        locationAnalysis = after.route.analysis.locationAnalysis,
        addedToNetwork = addedToNetwork,
        removedFromNetwork = removedFromNetwork,
        before = None,
        after = Some(after.toRouteData),
        removedWays = Seq.empty,
        addedWays = Seq.empty,
        updatedWays = Seq.empty,
        diffs = RouteDiff(
          factDiffs = factDiffs
        ),
        facts = Seq.empty,
        impactedNodeIds = impactedNodeIds
      )
    )
  }

  private def processDelete(context: ChangeSetContext, before: RouteAnalysis, routeId: Long): RouteChange = {

    analysisContext.data.routes.watched.delete(routeId)

    val routeInfo = before.route.copy(
      labels = before.route.labels.filterNot(_ == "active"),
      active = false
    )

    routeRepository.save(routeInfo)

    tileChangeAnalyzer.analyzeRoute(before)
    val impactedNodeIds: Seq[Long] = before.routeNodeAnalysis.routeNodes.map(_.node.id).distinct.sorted

    val addedToNetwork = context.changes.newNetworkChanges.flatMap { networkChanges =>
      if (networkChanges.relations.added.contains(routeId)) {
        Some(networkChanges.toRef)
      }
      else {
        None
      }
    }

    val removedFromNetwork = context.changes.newNetworkChanges.flatMap { networkChanges =>
      if (networkChanges.relations.removed.contains(routeId)) {
        Some(networkChanges.toRef)
      }
      else {
        None
      }
    }

    val key = context.buildChangeKey(routeId)

    RouteChangeStateAnalyzer.analyzed(
      RouteChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.Delete,
        name = before.route.summary.name,
        locationAnalysis = before.route.analysis.locationAnalysis,
        addedToNetwork = addedToNetwork,
        removedFromNetwork = removedFromNetwork,
        before = Some(before.toRouteData),
        after = None,
        removedWays = Seq.empty,
        addedWays = Seq.empty,
        updatedWays = Seq.empty,
        diffs = RouteDiff(),
        facts = Seq(Fact.Deleted),
        impactedNodeIds = impactedNodeIds
      )
    )
  }

  def processUpdate(context: ChangeSetContext, before: RouteAnalysis, after: RouteAnalysis, routeId: Long): RouteChange = {

    tileChangeAnalyzer.analyzeRouteChange(before, after)

    val routeUpdate = new RouteDiffAnalyzer(before, after).analysis

    if (routeUpdate.facts.contains(Fact.LostRouteTags)) {
      analysisContext.data.routes.watched.delete(routeUpdate.id)
    }

    //    val facts = if (routeUpdate.facts.contains(Fact.LostRouteTags)) {
    //      Seq(Fact.WasOrphan) ++ routeUpdate.facts
    //    }
    //    else {
    //      Seq(Fact.OrphanRoute) ++ routeUpdate.facts
    //    }
    val facts = routeUpdate.facts

    routeRepository.save(after.route)
    analysisContext.data.routes.watched.add(after.id, after.route.elementIds)
    val impactedNodeIds: Seq[Long] = Seq(before, after).flatMap { routeAnalysis =>
      routeAnalysis.routeNodeAnalysis.routeNodes.map(_.node.id)
    }.distinct.sorted

    val addedToNetwork = context.changes.newNetworkChanges.flatMap { networkChanges =>
      if (networkChanges.relations.added.contains(routeId)) {
        Some(networkChanges.toRef)
      }
      else {
        None
      }
    }

    val removedFromNetwork = context.changes.newNetworkChanges.flatMap { networkChanges =>
      if (networkChanges.relations.removed.contains(routeId)) {
        Some(networkChanges.toRef)
      }
      else {
        None
      }
    }

    val key = context.buildChangeKey(routeUpdate.after.id)

    RouteChangeStateAnalyzer.analyzed(
      RouteChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.Update,
        name = routeUpdate.after.name,
        locationAnalysis = after.route.analysis.locationAnalysis,
        addedToNetwork = addedToNetwork,
        removedFromNetwork = removedFromNetwork,
        before = Some(routeUpdate.before.toRouteData),
        after = Some(routeUpdate.after.toRouteData),
        removedWays = routeUpdate.removedWays,
        addedWays = routeUpdate.addedWays,
        updatedWays = routeUpdate.updatedWays,
        diffs = routeUpdate.diffs,
        facts = facts,
        impactedNodeIds = impactedNodeIds
      )
    )
  }
}
