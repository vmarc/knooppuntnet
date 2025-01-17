package kpn.server.analyzer.engine.changes.route

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Relation
import kpn.core.analysis.TagInterpreter
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.Label
import kpn.core.doc.RouteRelation
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.history.RouteTagDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileChangeAnalyzer
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeProcessor(
  analysisContext: AnalysisContext,
  routeChangeAnalyzer: RouteChangeAnalyzer,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  routeMainAnalyzer: RouteMainAnalyzer,
  tileChangeAnalyzer: RouteTileChangeAnalyzer,
  routeRepository: RouteRepository,
  rawDataRepository: RawDataRepository,
) {

  private val log = Log(classOf[BaseRouteChangeProcessor])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {

      val routeElementChanges = routeChangeAnalyzer.analyze(context)

      val deleteImpactedNodeIds = routeElementChanges.deletes.flatMap { routeId =>
        analysisContext.watched.routes.delete(routeId)
        routeRepository.findBaseRouteById(routeId) match {
          case Some(baseRouteDoc) =>

            routeRepository.saveBaseRoute(baseRouteDoc.deactivated)
            baseRouteDoc.nodes.nodeIds
          case None =>
            // TODO report?
            Seq.empty
        }
      }

      val createImpactedNodeIds = routeElementChanges.creates.flatMap { routeId =>
        rawDataRepository.route(context.changeSet.timestampAfter, routeId) match {
          case None =>
            // TODO report?
            println("route not found")
            Seq.empty
          case Some(rawRouteDoc) =>
            baseRouteMainAnalyzer.analyze(rawRouteDoc.relation, rawRouteDoc.structure) match {
              case None =>
                // TODO report?
                Seq.empty
              case Some(routeAnalysisContext) =>
                analysisContext.watched.routes.add(routeId, routeAnalysisContext.elementIds)
                val baseRouteDoc = new BaseRouteDocBuilder(routeAnalysisContext).build()
                routeRepository.saveBaseRoute(baseRouteDoc)
                baseRouteDoc.nodes.nodeIds
            }
        }
      }

      val updateImpactedNodeIds = routeElementChanges.updates.flatMap { routeId =>
        rawDataRepository.route(context.changeSet.timestampAfter, routeId) match {
          case None =>
            // TODO report?
            Seq.empty
          case Some(rawRouteDoc) =>
            val beforeOption = routeRepository.findBaseRouteById(routeId)
            analyzeBaseRoute(rawRouteDoc.relation, rawRouteDoc.structure) match {
              case None => Seq.empty
              case Some(baseRouteDoc) =>
                beforeOption match {
                  case None =>
                    baseRouteDoc.nodes.nodeIds

                  case Some(before) =>
                    val beforeNodeIds = before.nodes.nodeIds.toSet
                    val afterNodeIds = baseRouteDoc.nodes.nodeIds.toSet
                    val addedNodeIds = afterNodeIds -- beforeNodeIds
                    val removedNodeIds = beforeNodeIds -- afterNodeIds
                    (addedNodeIds ++ removedNodeIds).toSeq.sorted
                }
            }
        }
      }

      //      val batchSize = 50
      //      val changedRouteIds = routeElementChanges.elementIds
      //      if (changedRouteIds.nonEmpty) {
      //        log.info(s"${changedRouteIds.size} route(s) impacted: ${changedRouteIds.mkString(", ")}")
      //      }
      //      val updatedContext = processRouteIds(context, changedRouteIds)

      val impactedNodeIds = (deleteImpactedNodeIds ++ createImpactedNodeIds ++ updateImpactedNodeIds).distinct.sorted
      val impactedRouteIds = routeElementChanges.elementIds

      val updatedContext = context.copy(
        baseRouteCreatedIds = routeElementChanges.creates,
        baseRouteUpdatedIds = routeElementChanges.updates,
        baseRouteDeletedIds = routeElementChanges.deletes,
      ).withImpact(nodeIds = impactedNodeIds, routeIds = impactedRouteIds)

      (
        s"${routeElementChanges.elementIds.size} base routes",
        updatedContext
      )
    }
  }

  //  private def processRouteIds(context: ChangeSetContext, routeIds: Seq[Long]): ChangeSetContext = {
  //    Log.context("base-routes") {
  //      val routeCount = routeIds.size
  //      log.info(s"analyzing $routeCount base routes")
  //      val logContext = Log.contextMessages
  //      log.infoElapsed {
  //        ThreadExecutor.execute(10, routeIds) { (index, count, routeId) =>
  //          Log.context(logContext) {
  //            Log.context(s"$index/$count $routeId") {
  //              analysisContext.watched.routes.delete(routeId)
  //              log.infoElapsed {
  //                try {
  //                  rawDataRepository.route(context.changeSet.timestampAfter, routeId) match {
  //                    case Some(rawRouteDoc) =>
  //                      analyzeBaseRoute(rawRouteDoc.relation, rawRouteDoc.structure)
  //                    case None =>
  //                      log.error(s"route $routeId not found in route-relations")
  //                  }
  //                } catch {
  //                  case e: Exception =>
  //                    log.error(s"Error analyzing detail route $routeId", e)
  //                }
  //                (s"Analyzed route $routeId", ())
  //              }
  //            }
  //          }
  //        }
  //        (s"Analyzed $routeCount routes", ())
  //      }
  //    }
  //
  //    val baseRouteCreatedIds: Seq[Long] = Seq.empty
  //    val baseRouteUpdatedIds: Seq[Long] = Seq.empty
  //    val baseRouteDeletedIds: Seq[Long] = Seq.empty
  //
  //    context.copy(
  //      baseRouteCreatedIds = baseRouteCreatedIds,
  //      baseRouteUpdatedIds = baseRouteUpdatedIds,
  //      baseRouteDeletedIds = baseRouteDeletedIds,
  //    )
  //  }

  private def analyzeBaseRoute(relation: Relation, hierarchy: Option[RouteRelation]): Option[BaseRouteDoc] = {
    baseRouteMainAnalyzer.analyze(relation, hierarchy) match {
      case None => None
      case Some(context) =>
        val baseRouteDoc = new BaseRouteDocBuilder(context).build()
        analysisContext.watched.routes.add(relation.id, context.elementIds)
        routeRepository.saveBaseRoute(baseRouteDoc)
        context.tileDatas.foreach { tileData =>
          val doc = RouteTileDoc(
            _id = s"${tileData.name}-${context.relation.id}",
            routeId = context.relation.id,
            routeName = context.routeNameAnalysis.name.getOrElse("no-name"), // TODO redesign tiles - can do better?
            routeTypes = context.routeTypes,
            z = tileData.z,
            x = tileData.x,
            y = tileData.y,
            layer = tileData.layer,
            scope = tileData.scope,
            survey = tileData.survey,
            error = tileData.error,
            segments = tileData.segments
          )
          routeRepository.saveRouteTile(doc)
        }
        Some(baseRouteDoc)
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
        )
      )
    )
  }
}
