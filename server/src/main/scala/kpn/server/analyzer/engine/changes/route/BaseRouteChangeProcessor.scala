package kpn.server.analyzer.engine.changes.route

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Relation
import kpn.core.analysis.TagInterpreter
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.Label
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.history.RouteTagDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileChangeAnalyzer
import kpn.server.overpass.OverpassRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

import scala.concurrent.ExecutionContext

@Component
class BaseRouteChangeProcessor(
  analysisContext: AnalysisContext,
  routeChangeAnalyzer: RouteChangeAnalyzer,
  overpassRepository: OverpassRepository,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  routeMainAnalyzer: RouteMainAnalyzer,
  tileChangeAnalyzer: RouteTileChangeAnalyzer,
  routeRepository: RouteRepository,
  implicit val analysisExecutionContext: ExecutionContext
) {

  private val log = Log(classOf[BaseRouteChangeProcessor])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {

      val routeElementChanges = routeChangeAnalyzer.analyze(context)
      val batchSize = 50
      val changedRouteIds = routeElementChanges.elementIds
      if (changedRouteIds.nonEmpty) {
        log.info(s"${changedRouteIds.size} route(s) impacted: ${changedRouteIds.mkString(", ")}")
      }
      val routeChanges = changedRouteIds.sliding(batchSize, batchSize).flatMap { routeIds =>
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
    data.before match {
      case None =>
        data.after match {
          case None => None // TODO message ?
          case Some(after) =>
            if (TagInterpreter.isRouteRelation(after)) {
              processCreate(context, after, data.routeId)
            }
            else {
              None
            }
        }
      case Some(before) =>
        data.after match {
          case None => processDelete(context, before, data.routeId)
          case Some(after) =>
            if (TagInterpreter.isRouteRelation(before)) {
              processUpdate(context, before, after, data.routeId)
            }
            else {
              processCreate(context, after, data.routeId)
            }
        }
    }
  }

  private def processCreate(context: ChangeSetContext, relationAfter: Relation, routeId: Long): Option[RouteChange] = {

    baseRouteMainAnalyzer.analyze(relationAfter, None /* TODO redesign - hierarchy */).map { contextAfter =>
      val afterBaseRouteDoc = new BaseRouteDocBuilder(contextAfter).build()
      routeRepository.saveBaseRoute(afterBaseRouteDoc)
      // TODO redesign - move to phase 2
      routeMainAnalyzer.analyze(afterBaseRouteDoc) match {
        case Some(routeDoc) => routeRepository.saveRoute(routeDoc)
        case None =>
      }

      analysisContext.watched.routes.add(routeId, afterBaseRouteDoc.elementIds)

      val factDiffs = if (afterBaseRouteDoc.facts.nonEmpty) {
        Some(
          FactDiffs(
            introduced = afterBaseRouteDoc.facts
          )
        )
      }
      else {
        None
      }

      val impactedNodeIds: Seq[Long] = contextAfter.routeNodesAnalysis.nodes.map(_.node.id).distinct.sorted

      val key = context.buildChangeKey(routeId)

      val addedToNetwork = context.changes.networkChanges.flatMap { networkChanges =>
        if (networkChanges.relations.added.contains(routeId)) {
          Some(networkChanges.toRef)
        }
        else {
          None
        }
      }

      val removedFromNetwork = context.changes.networkChanges.flatMap { networkChanges =>
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
          name = afterBaseRouteDoc.summary.name,
          locationAnalysis = afterBaseRouteDoc.locationAnalysis,
          addedToNetwork = addedToNetwork,
          removedFromNetwork = removedFromNetwork,
          before = None,
          after = Some(RouteData.from(contextAfter)),
          removedWays = Seq.empty,
          addedWays = Seq.empty,
          updatedWays = Seq.empty,
          diffs = RouteDiff(
            factDiffs = factDiffs
          ),
          facts = Seq.empty,
          impactedNodeIds = impactedNodeIds,
          afterBaseRouteDoc.tiles
        )
      )
    }
  }

  private def processDelete(context: ChangeSetContext, relationBefore: Relation, routeId: Long): Option[RouteChange] = {

    analysisContext.watched.routes.delete(routeId)

    baseRouteMainAnalyzer.analyze(relationBefore, None /* TODO redesign - hierarchy */).map { contextBefore =>
      val baseRouteDoc = new BaseRouteDocBuilder(contextBefore).build().deactivated
      routeRepository.saveBaseRoute(baseRouteDoc)
      // TODO redesign - move to phase 2
      routeMainAnalyzer.analyze(baseRouteDoc) match {
        case Some(routeDoc) => routeRepository.saveRoute(routeDoc)
        case None =>
      }
      val impactedNodeIds: Seq[Long] = contextBefore.routeNodesAnalysis.nodes.map(_.node.id).distinct.sorted

      val addedToNetwork = context.changes.networkChanges.flatMap { networkChanges =>
        if (networkChanges.relations.added.contains(routeId)) {
          Some(networkChanges.toRef)
        }
        else {
          None
        }
      }

      val removedFromNetwork = context.changes.networkChanges.flatMap { networkChanges =>
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
          name = baseRouteDoc.summary.name,
          locationAnalysis = baseRouteDoc.locationAnalysis,
          addedToNetwork = addedToNetwork,
          removedFromNetwork = removedFromNetwork,
          before = Some(RouteData.from(contextBefore)),
          after = None,
          removedWays = Seq.empty,
          addedWays = Seq.empty,
          updatedWays = Seq.empty,
          diffs = RouteDiff(),
          facts = Seq(Fact.Deleted),
          impactedNodeIds = impactedNodeIds,
          baseRouteDoc.tiles
        )
      )
    }
  }

  def processUpdate(context: ChangeSetContext, relationBefore: Relation, relationAfter: Relation, routeId: Long): Option[RouteChange] = {

    val lostRouteTags = TagInterpreter.isRouteRelation(relationBefore) &&
      !TagInterpreter.isRouteRelation(relationAfter)

    baseRouteMainAnalyzer.analyze(relationBefore, None /* TODO redesign - hierarchy */) match {
      case None => None
      case Some(contextBefore) =>
        val baseRouteDocBefore = new BaseRouteDocBuilder(contextBefore).build()
        if (lostRouteTags) {
          processLostRouteTags(context, contextBefore, baseRouteDocBefore, relationAfter, routeId)
        }
        else {
          baseRouteMainAnalyzer.analyze(relationAfter, None /* TODO redesign - hierarchy */) match {
            case None => None
            case Some(contextAfter) =>
              val baseRouteDocAfter = new BaseRouteDocBuilder(contextAfter).build()

              val impactedTiles = tileChangeAnalyzer.impactedTiles(contextBefore, contextAfter)

              val routeUpdate = new RouteDiffAnalyzer(RouteData.from(contextBefore), RouteData.from(contextAfter)).analysis

              if (routeUpdate.facts.contains(Fact.LostRouteTags)) {
                analysisContext.watched.routes.delete(routeUpdate.id)
              }
              else {
                analysisContext.watched.routes.add(contextAfter.relation.id, contextAfter.elementIds)
              }

              val facts = routeUpdate.facts

              routeRepository.saveBaseRoute(baseRouteDocAfter)
              // TODO redesign - move to phase 2
              routeMainAnalyzer.analyze(baseRouteDocAfter) match {
                case Some(routeDoc) => routeRepository.saveRoute(routeDoc)
                case None =>
              }

              val impactedNodeIds: Seq[Long] = Seq(contextBefore, contextAfter).flatMap { routeAnalysis =>
                routeAnalysis.routeNodesAnalysis.nodes.map(_.node.id)
              }.distinct.sorted

              val addedToNetwork = context.changes.networkChanges.flatMap { networkChanges =>
                if (networkChanges.relations.added.contains(routeId)) {
                  Some(networkChanges.toRef)
                }
                else {
                  None
                }
              }

              val removedFromNetwork = context.changes.networkChanges.flatMap { networkChanges =>
                if (networkChanges.relations.removed.contains(routeId)) {
                  Some(networkChanges.toRef)
                }
                else {
                  None
                }
              }

              val key = context.buildChangeKey(routeUpdate.after.relationId)

              Some(
                RouteChangeStateAnalyzer.analyzed(
                  RouteChange(
                    _id = key.toId,
                    key = key,
                    changeType = ChangeType.Update,
                    name = routeUpdate.after.name,
                    locationAnalysis = baseRouteDocAfter.locationAnalysis,
                    addedToNetwork = addedToNetwork,
                    removedFromNetwork = removedFromNetwork,
                    before = Some(routeUpdate.before),
                    after = Some(routeUpdate.after),
                    removedWays = routeUpdate.removedWays,
                    addedWays = routeUpdate.addedWays,
                    updatedWays = routeUpdate.updatedWays,
                    diffs = routeUpdate.diffs,
                    facts = facts,
                    impactedNodeIds = impactedNodeIds,
                    impactedTiles
                  )
                )
              )
          }
        }
    }
  }

  private def processLostRouteTags(
    context: ChangeSetContext,
    beforeContext: BaseRouteAnalysisContext,
    beforeBaseRouteDoc: BaseRouteDoc,
    relationAfter: Relation,
    routeId: Long
  ): Option[RouteChange] = {

    analysisContext.watched.routes.delete(routeId)

    val updatedRouteDoc = beforeBaseRouteDoc.copy(
      labels = beforeBaseRouteDoc.labels.filterNot(_ == Label.active),
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

    val tagDiffs = new RouteTagDiffAnalyzer(beforeContext.relation, relationAfter).diffs

    val key = context.buildChangeKey(routeId)

    Some(
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
          removedWays = Seq.empty,
          addedWays = Seq.empty,
          updatedWays = Seq.empty,
          diffs = RouteDiff(
            tagDiffs = tagDiffs
          ),
          facts = Seq(Fact.LostRouteTags),
          impactedNodeIds = impactedNodeIds,
          beforeBaseRouteDoc.tiles
        )
      )
    )
  }
}
