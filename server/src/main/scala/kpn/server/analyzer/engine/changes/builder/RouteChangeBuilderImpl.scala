package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.route.RouteDiff
import kpn.core.analysis.Network
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.RouteMainAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteChangeStateAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteFactAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteUtil
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileChangeAnalyzer
import kpn.server.repository.RouteDetailRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteChangeBuilderImpl(
  analysisContext: AnalysisContext,
  routeRepository: RouteRepository,
  routeDetailRepository: RouteDetailRepository,
  tileChangeAnalyzer: RouteTileChangeAnalyzer,
  routeMainAnalyzer: RouteMainAnalyzer
) extends RouteChangeBuilder {

  private val log = Log(classOf[RouteChangeBuilderImpl])

  override def build(context: ChangeBuilderContext): Seq[RouteChange] = {

    val routeIdsBefore = routeIdsIn(context.networkBefore)
    val routeIdsAfter = routeIdsIn(context.networkAfter)

    val addedRouteIds = routeIdsAfter -- routeIdsBefore
    val removedRouteIds = routeIdsBefore -- routeIdsAfter
    val commonRouteIds = routeIdsBefore intersect routeIdsAfter

    routeChangesAdded(context, addedRouteIds) ++
      routeChangesRemoved(context, removedRouteIds) ++
      routeChangesUpdated(context, commonRouteIds)
  }

  private def routeChangesAdded(context: ChangeBuilderContext, routeIds: Set[Long]): Seq[RouteChange] = {

    val routeDatasAfter = routeAnalysesIn(context.networkAfter, routeIds)

    routeDatasAfter.map { routeDataAfter =>

      val routeId = routeDataAfter.relationId

      analysisContext.watched.routes.delete(routeId)

      context.routeAnalysisBefore.find(_.relation.id == routeId) match {

        case None =>

          /*
            Cannot find 'before' in database; this must be version 1 of the route.
           */
          RouteUtil.assertVersion1(routeDataAfter)

          val impactedNodeIds: Seq[Long] = routeDataAfter.networkNodes.map(_.nodeId).distinct.sorted

          val key = context.changeSetContext.buildChangeKey(routeDataAfter.relationId)
          RouteChangeStateAnalyzer.analyzed(
            RouteChange(
              _id = key.toId,
              key = key,
              changeType = ChangeType.Create,
              name = routeDataAfter.name,
              locationAnalysis = routeDataAfter.locationAnalysis,
              addedToNetwork = context.networkAfter.map(_.toRef).toSeq,
              removedFromNetwork = Seq.empty,
              before = None,
              after = Some(routeDataAfter),
              removedWays = Seq.empty,
              addedWays = Seq.empty,
              updatedWays = Seq.empty,
              diffs = RouteDiff(),
              facts = Seq.empty,
              impactedNodeIds,
              routeDataAfter.tiles
            )
          )

        case Some(analysisBefore) =>

          val routeUpdate = new RouteDiffAnalyzer(RouteData.from(analysisBefore), routeDataAfter).analysis

          val impactedTiles: Seq[String] = Seq.empty // TODO redesign - tileChangeAnalyzer.impactedTiles(analysisBefore, analysisAfter)

          val impactedNodeIds: Seq[Long] = (analysisBefore.routeNodesAnalysis.nodes.map(_.node.id) ++
            routeDataAfter.networkNodes.map(_.nodeId)).distinct.sorted

          val key = context.changeSetContext.buildChangeKey(routeId)
          RouteChangeStateAnalyzer.analyzed(
            RouteChange(
              _id = key.toId,
              key = key,
              changeType = ChangeType.Update,
              name = routeDataAfter.name,
              locationAnalysis = routeDataAfter.locationAnalysis,
              addedToNetwork = context.networkAfter.map(_.toRef).toSeq,
              removedFromNetwork = Seq.empty,
              before = Some(RouteData.from(analysisBefore)),
              after = Some(routeDataAfter),
              removedWays = routeUpdate.removedWays,
              addedWays = routeUpdate.addedWays,
              updatedWays = routeUpdate.updatedWays,
              diffs = routeUpdate.diffs,
              facts = routeUpdate.facts,
              impactedNodeIds,
              impactedTiles
            )
          )
      }
    }
  }

  private def routeChangesRemoved(context: ChangeBuilderContext, routeIds: Set[Long]): Seq[RouteChange] = {

    val routeDatasBefore = routeAnalysesIn(context.networkBefore, routeIds)

    routeDatasBefore.flatMap { dataBefore =>

      val routeId = dataBefore.relationId

      context.routeAnalysisAfter.find(_.relation.id == routeId) match {

        case None =>

          /*
              We cannot load the route from Overpass at the 'after' timestamp, we assume that
              the route is flagged as deleted in OpenStreetMap. If the RouteChange does not
              have a 'Deleted' fact yet (for example when the RouteChange was generated when
              the route was no longer referenced from a network), we add it now.
           */
          //noinspection SideEffectsInMonadicTransformation
          log.debug(s"OK: route '$routeId' has been deleted from the database.")

          val routeDetailDoc = ??? // TODO redesign - read doc from database?
          // dataBefore.routeDetail.deactivated.copy(
          //    analysis = RouteInfoAnalysis(
          //      expectedName = "",
          //      map = RouteMap(),
          //      structureStrings = Seq.empty,
          //    ),
          //    lastUpdated = context.changeSetContext.changeSet.timestamp,
          //    geometryDigest = "",
          //    locationAnalysis = RouteLocationAnalysis(None, Seq.empty, Seq.empty)
          //  )

          routeDetailRepository.save(routeDetailDoc)
          routeMainAnalyzer.analyze(routeDetailDoc) match {
            case Some(routeDoc) => routeRepository.saveRoute(routeDoc)
            case None =>
          }
          val impactedNodeIds: Seq[Long] = dataBefore.networkNodes.map(_.nodeId).distinct.sorted

          val key = context.changeSetContext.buildChangeKey(routeId)
          Some(
            RouteChangeStateAnalyzer.analyzed(
              RouteChange(
                _id = key.toId,
                key = key,
                changeType = ChangeType.Delete,
                name = dataBefore.name,
                locationAnalysis = dataBefore.locationAnalysis,
                addedToNetwork = Seq.empty,
                removedFromNetwork = context.networkBefore.map(_.toRef).toSeq,
                before = Some(dataBefore),
                after = None,
                removedWays = Seq.empty,
                addedWays = Seq.empty,
                updatedWays = Seq.empty,
                diffs = RouteDiff(),
                facts = Seq(Fact.Deleted),
                impactedNodeIds,
                dataBefore.tiles
              )
            )
          )

        case Some(analysisAfter) =>
          processRemovedRoute(context, dataBefore, RouteData.from(analysisAfter))
      }
    }
  }

  private def processRemovedRoute(context: ChangeBuilderContext, dataBefore: RouteData, dataAfter: RouteData): Option[RouteChange] = {

    val routeId = dataBefore.relationId
    val routeUpdate = new RouteDiffAnalyzer(dataBefore, dataAfter).analysis

    val facts = new RouteFactAnalyzer(analysisContext.watched).facts(Some(dataBefore), dataAfter).filter(f => f == Fact.LostRouteTags)

    //    val elementIds = RelationAnalyzer.toElementIds(dataAfter.relation)
    //    analysisContext.watched.routes.add(routeId, elementIds)
    //
    //    routeRepository.saveRouteDetail(dataAfter.routeDetail.copy(/*orphan = true*/))
    //    // TODO redesign - move to phase 2
    //    routeMainAnalyzer.analyze(dataAfter.routeDetail.copy(/*orphan = true*/)) match {
    //      case Some(routeDoc) => routeRepository.saveRoute(routeDoc)
    //      case None =>
    //    }

    //        analysisAfter.routeNodes.routeNodes.foreach { routeNode =>
    //          val country = countryAnalyzer.country(Seq(routeNode.node))
    //          val loadedNode = LoadedNode.from(country, routeNode.node.raw)
    //          val nodeInfo = NodeInfoBuilder.fromLoadedNode(loadedNode)
    //          analysisRepository.saveNode(nodeInfo)
    //        }

    val impactedTiles: Seq[String] = Seq.empty // TODO redesign - tileChangeAnalyzer.impactedTiles(analysisBefore, analysisAfter)

    val impactedNodeIds: Seq[Long] = (dataBefore.networkNodes.map(_.nodeId) ++
      dataAfter.networkNodes.map(_.nodeId)).distinct.sorted

    val key = context.changeSetContext.buildChangeKey(routeId)
    Some(
      RouteChangeStateAnalyzer.analyzed(
        RouteChange(
          _id = key.toId,
          key = key,
          changeType = ChangeType.Update,
          name = dataAfter.name,
          locationAnalysis = dataAfter.locationAnalysis,
          addedToNetwork = Seq.empty,
          removedFromNetwork = context.networkBefore.map(_.toRef).toSeq,
          before = Some(dataBefore),
          after = Some(dataAfter),
          removedWays = routeUpdate.removedWays,
          addedWays = routeUpdate.addedWays,
          updatedWays = routeUpdate.updatedWays,
          diffs = routeUpdate.diffs,
          facts = facts,
          impactedNodeIds,
          impactedTiles
        )
      )
    )
  }

  private def routeChangesUpdated(context: ChangeBuilderContext, routeIds: Set[Long]): Seq[RouteChange] = {

    val routeDatasBefore = routeAnalysesIn(context.networkBefore, routeIds)
    val routeDatasAfter = routeAnalysesIn(context.networkAfter, routeIds)

    routeDatasBefore.flatMap { routeDataBefore =>

      val routeId = routeDataBefore.relationId

      routeDatasAfter.find(_.relationId == routeId).flatMap { routeDataAfter =>

        val impactedNodeIds: Seq[Long] = (routeDataBefore.networkNodes.map(_.nodeId) ++
          routeDataAfter.networkNodes.map(_.nodeId)).distinct.sorted

        val routeUpdate = new RouteDiffAnalyzer(routeDataBefore, routeDataAfter).analysis

        if (routeUpdate.nonEmpty) {

          val impactedTiles: Seq[String] = Seq.empty // TODO redesign - tileChangeAnalyzer.impactedTiles(analysisBefore, analysisAfter)

          val key = context.changeSetContext.buildChangeKey(routeId)
          Some(
            RouteChangeStateAnalyzer.analyzed(
              RouteChange(
                _id = key.toId,
                key = key,
                changeType = ChangeType.Update,
                name = routeDataAfter.name,
                locationAnalysis = routeDataAfter.locationAnalysis,
                addedToNetwork = Seq.empty,
                removedFromNetwork = Seq.empty,
                before = Some(routeDataBefore),
                after = Some(routeDataAfter),
                removedWays = routeUpdate.removedWays,
                addedWays = routeUpdate.addedWays,
                updatedWays = routeUpdate.updatedWays,
                diffs = routeUpdate.diffs,
                facts = routeUpdate.facts,
                impactedNodeIds,
                impactedTiles
              )
            )
          )
        }
        else {
          None
        }
      }
    }
  }

  private def routeIdsIn(network: Option[Network]): Set[Long] = {
    network.toSeq.flatMap(_.routes.map(_.id)).toSet
  }

  private def routeAnalysesIn(network: Option[Network], routeIds: Set[Long]): Seq[RouteData] = {
    network.toSeq.flatMap(_.routes.filter(route => routeIds.contains(route.id))).map(_.data)
  }
}
