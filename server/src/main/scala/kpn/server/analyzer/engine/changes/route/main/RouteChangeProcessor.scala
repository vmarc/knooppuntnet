package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.WayDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Relation
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.history.RouteTagDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.overpass.OverpassRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

import scala.concurrent.ExecutionContext

@Component
class RouteChangeProcessor(
  analysisContext: AnalysisContext,
  overpassRepository: OverpassRepository,
  routeMainAnalyzer: RouteMainAnalyzer,
  routeRepository: RouteRepository,
  implicit val analysisExecutionContext: ExecutionContext
) extends ChangeProcessor {

  private val log = Log(classOf[RouteChangeProcessor])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {

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
            None
          case Some(baseRouteDoc) =>
            if (baseRouteDoc.active) {
              routeMainAnalyzer.analyze(baseRouteDoc) match {
                case None =>
                  // TODO delete?
                  None

                case Some(routeDocAfter) =>
                  // CREATE
                  processCreate(context, routeDocAfter, routeId)
              }
            }
            else {
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
                if (context.baseRouteDeletedIds.contains(routeId)) {
                  processDelete(context, before)
                }
                else {
                  processUpdate(context, before, routeDoc, routeId)
                }
            }
        }
    }
  }

  private def processBatch(context: ChangeSetContext, routeElementChanges: ElementChanges, routeIds: Seq[Long]): Seq[RouteChange] = {
    val routeChangeDatas = readBeforeAndAfter(context, routeIds)
    routeChangeDatas.flatMap { routeChangeData =>
      processChangeData(context, routeChangeData)
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

  private def processChangeData(context: ChangeSetContext, data: RouteChangeData): Option[RouteChange] = {
    //    data.before match {
    //      case None =>
    //        data.after match {
    //          case None => None // TODO message ?
    //          case Some(after) =>
    //            if (TagInterpreter.isRouteRelation(after)) {
    //              processCreate(context, after, data.routeId)
    //            }
    //            else {
    //              None
    //            }
    //        }
    //      case Some(before) =>
    //        data.after match {
    //          case None => None // TODO processDelete(context, before, data.routeId)
    //          case Some(after) =>
    //            if (TagInterpreter.isRouteRelation(before)) {
    //              processUpdate(context, before, after, data.routeId)
    //            }
    //            else {
    //              processCreate(context, after, data.routeId)
    //            }
    //        }
    //    }
    None
  }

  private def processCreate(context: ChangeSetContext, routeDocAfter: RouteDoc, routeId: Long): Option[RouteChangeContext] = {

    routeRepository.saveRoute(routeDocAfter)
    val factDiffs = Option.when(routeDocAfter.facts.nonEmpty) {
      FactDiffs(
        introduced = routeDocAfter.facts
      )
    }

    val impactedNodeIds: Seq[Long] = routeDocAfter.nodes.nodeIds

    val key = context.buildChangeKey(routeId)

    val addedToNetwork = routeDocAfter.networkReferences.map(_.toRef)
    val impactedNetworkIds = addedToNetwork.map(_.id)

    val baseRouteChangeOption = context.changes.baseRouteChanges.find(_.routeId == routeId)
    val wayDiffs = baseRouteChangeOption match {
      case Some(baseRouteChange) => baseRouteChange.wayDiffs
      case None => WayDiffs.empty
    }

    Some(
      RouteChangeContext(
        RouteChangeStateAnalyzer.analyzed(
          RouteChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Create,
            name = routeDocAfter.summary.name,
            locationAnalysis = routeDocAfter.locationAnalysis,
            addedToNetwork = addedToNetwork,
            removedFromNetwork = Seq.empty,
            before = None,
            after = Some(RouteData.from(routeDocAfter)),
            wayDiffs,
            diffs = RouteDiff(
              factDiffs = factDiffs
            ),
            facts = Seq.empty,
          )
        ),
        impactedNodeIds = impactedNodeIds,
        impactedNetworkIds = impactedNetworkIds
      )
    )
  }

  private def processDelete(context: ChangeSetContext, routeDoc: RouteDoc): Option[RouteChangeContext] = {

    routeRepository.saveRoute(routeDoc.deactivated)

    val impactedNodeIds: Seq[Long] = routeDoc.nodes.nodeIds.sorted

    val removedFromNetwork = routeDoc.networkReferences.map(_.toRef)
    val impactedNetworkIds = removedFromNetwork.map(_.id)

    val key = context.buildChangeKey(routeDoc._id)

    Some(
      RouteChangeContext(
        RouteChangeStateAnalyzer.analyzed(
          RouteChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Delete,
            name = routeDoc.summary.name,
            locationAnalysis = routeDoc.locationAnalysis,
            addedToNetwork = Seq.empty,
            removedFromNetwork = removedFromNetwork,
            before = Some(RouteData.from(routeDoc)),
            after = None,
            wayDiffs = WayDiffs.empty,
            diffs = RouteDiff(),
            facts = Seq(Fact.Deleted),
          )
        ),
        impactedNodeIds = impactedNodeIds,
        impactedNetworkIds = impactedNetworkIds
      )
    )
  }

  def processUpdate(context: ChangeSetContext, before: RouteDoc, after: RouteDoc, routeId: Long): Option[RouteChangeContext] = {

    //    val lostRouteTags = TagInterpreter.isRouteRelation(relationBefore) &&
    //      !TagInterpreter.isRouteRelation(relationAfter)

    //    baseRouteMainAnalyzer.analyze(relationBefore, None /* TODO redesign - subRelationTree */) match {
    //      case None => None
    //      case Some(contextBefore) =>
    //        val baseRouteDocBefore = new BaseRouteDocBuilder(contextBefore).build()
    //        if (lostRouteTags) {
    //          processLostRouteTags(context, contextBefore, baseRouteDocBefore, relationAfter, routeId)
    //        }
    //        else {
    //          baseRouteMainAnalyzer.analyze(relationAfter, None /* TODO redesign - subRelationTree */) match {
    //            case None => None
    //            case Some(contextAfter) =>
    //              val baseRouteDocAfter = new BaseRouteDocBuilder(contextAfter).build()

    //              val impactedTiles = tileChangeAnalyzer.impactedTiles(contextBefore, contextAfter)

    val baseRouteChangeOption = context.changes.baseRouteChanges.find(_.routeId == routeId)

    val routeUpdate = new RouteDiffAnalyzer(RouteData.from(before), RouteData.from(after), baseRouteChangeOption).analysis

    //    if (routeUpdate.facts.contains(Fact.LostRouteTags)) {
    //      analysisContext.watched.routes.delete(routeUpdate.id)
    //    }
    //    else {
    //      analysisContext.watched.routes.add(contextAfter.relation.id, contextAfter.elementIds)
    //    }
    //
    val facts = routeUpdate.facts

    routeRepository.saveRoute(after)

    val impactedNodeIds: Seq[Long] = (before.nodes.nodeIds ++ after.nodes.nodeIds).distinct.sorted

    val beforeNetworkIds = before.networkReferences.map(_.id).toSet
    val afterNetworkIds = after.networkReferences.map(_.id).toSet

    val addedNetworkIds = (afterNetworkIds -- beforeNetworkIds).toSeq.sorted
    val removedNetworkIds = (beforeNetworkIds -- afterNetworkIds).toSeq.sorted
    val impactedNetworkIds = (beforeNetworkIds ++ afterNetworkIds).toSeq.sorted
    val addedToNetwork = after.networkReferences.filter(r => addedNetworkIds.contains(r.id)).map(_.toRef)
    val removedFromNetwork = before.networkReferences.filter(r => removedNetworkIds.contains(r.id)).map(_.toRef)

    val key = context.buildChangeKey(routeId)

    Some(
      RouteChangeContext(
        RouteChangeStateAnalyzer.analyzed(
          RouteChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Update,
            name = after.summary.name,
            locationAnalysis = after.locationAnalysis,
            addedToNetwork = addedToNetwork,
            removedFromNetwork = removedFromNetwork,
            before = Some(routeUpdate.before),
            after = Some(routeUpdate.after),
            wayDiffs = routeUpdate.wayDiffs,
            diffs = routeUpdate.diffs,
            facts = routeUpdate.facts,
          )
        ),
        impactedNodeIds = impactedNodeIds,
        impactedNetworkIds = impactedNetworkIds,
      )
    )
  }

  private def processLostRouteTags(
    context: ChangeSetContext,
    beforeContext: BaseRouteAnalysisContext,
    beforeBaseRouteDoc: BaseRouteDoc,
    relationAfter: Relation,
    routeId: Long
  ): Option[RouteChangeContext] = {

    analysisContext.watched.routes.delete(routeId)

    val updatedRouteDoc = beforeBaseRouteDoc.copy(
      active = false,
      facts = Seq(Fact.LostRouteTags)
    )

    routeRepository.saveBaseRoute(updatedRouteDoc)
    // TODO redesign - move to phase 2
    routeMainAnalyzer.analyze(updatedRouteDoc) match {
      case Some(routeDoc) => routeRepository.saveRoute(routeDoc)
      case None =>
    }

    val impactedNodeIds = beforeContext.routeNodesAnalysis.nodes.map(_.node.id).distinct.sorted

    val removedFromNetwork = context.changes.networkChanges.flatMap { networkChanges =>
      if (networkChanges.relations.removed.contains(routeId)) {
        Some(networkChanges.toRef)
      }
      else {
        None
      }
    }
    val impactedNetworkIds = removedFromNetwork.map(_.id)

    val tagDiffs = new RouteTagDiffAnalyzer(beforeContext.relation, relationAfter).diffs

    val key = context.buildChangeKey(routeId)

    Some(
      RouteChangeContext(

        RouteChangeStateAnalyzer.analyzed(
          RouteChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Delete,
            name = beforeBaseRouteDoc.summary.name,
            locationAnalysis = beforeBaseRouteDoc.locationAnalysis,
            addedToNetwork = Seq.empty,
            removedFromNetwork = removedFromNetwork,
            before = Some(RouteData.from(beforeContext)),
            after = None,
            wayDiffs = WayDiffs.empty,
            diffs = RouteDiff(
              tagDiffs = tagDiffs
            ),
            facts = Seq(Fact.LostRouteTags)
          )
        ),
        impactedNodeIds = impactedNodeIds,
        impactedNetworkIds = impactedNetworkIds,
      )
    )
  }
}
