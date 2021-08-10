package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteMap
import kpn.api.custom.Fact
import kpn.core.analysis.Network
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteChangeStateAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteFactAnalyzer
import kpn.server.analyzer.engine.changes.route.RouteUtil
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.TileChangeAnalyzer
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteChangeBuilderImpl(
  analysisContext: AnalysisContext,
  routeRepository: RouteRepository,
  tileChangeAnalyzer: TileChangeAnalyzer
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

    val analysesAfter = routeAnalysesIn(context.networkAfter, routeIds)

    analysesAfter.map { analysisAfter =>

      val routeId = analysisAfter.id

      val extraFacts = Seq(
        if (analysisContext.data.routes.watched.contains(routeId)) {
          analysisContext.data.routes.watched.delete(routeId)
          Seq(Fact.WasOrphan)
        }
        else {
          Seq.empty
        }
      ).flatten

      context.routeAnalysesBefore.find(_.id == routeId) match {

        case None =>

          /*
            Cannot find 'before' in database; this must be version 1 of the route.
           */
          RouteUtil.assertVersion1(analysisAfter)

          tileChangeAnalyzer.analyzeRoute(analysisAfter)

          throw new IllegalStateException("Unexpected code execution - would need to calculate impactedNodeIds here")
          val impactedNodeIds: Seq[Long] = Seq.empty

          val key = context.changeSetContext.buildChangeKey(analysisAfter.id)
          RouteChangeStateAnalyzer.analyzed(
            RouteChange(
              _id = key.toId,
              key = key,
              changeType = ChangeType.Create,
              name = analysisAfter.name,
              locationAnalysis = analysisAfter.route.analysis.locationAnalysis,
              addedToNetwork = context.networkAfter.map(_.toRef).toSeq,
              removedFromNetwork = Seq.empty,
              before = None,
              after = Some(analysisAfter.toRouteData),
              removedWays = Seq.empty,
              addedWays = Seq.empty,
              updatedWays = Seq.empty,
              diffs = RouteDiff(),
              facts = extraFacts,
              impactedNodeIds
            )
          )

        case Some(analysisBefore) =>

          val routeUpdate = new RouteDiffAnalyzer(analysisBefore, analysisAfter).analysis

          tileChangeAnalyzer.analyzeRouteChange(analysisBefore, analysisAfter)

          throw new IllegalStateException("Unexpected code execution - would need to calculate impactedNodeIds here")
          val impactedNodeIds: Seq[Long] = Seq.empty

          val key = context.changeSetContext.buildChangeKey(routeId)
          RouteChangeStateAnalyzer.analyzed(
            RouteChange(
              _id = key.toId,
              key = key,
              changeType = ChangeType.Update,
              name = analysisAfter.name,
              locationAnalysis = analysisAfter.route.analysis.locationAnalysis,
              addedToNetwork = context.networkAfter.map(_.toRef).toSeq,
              removedFromNetwork = Seq.empty,
              before = Some(analysisBefore.toRouteData),
              after = Some(analysisAfter.toRouteData),
              removedWays = routeUpdate.removedWays,
              addedWays = routeUpdate.addedWays,
              updatedWays = routeUpdate.updatedWays,
              diffs = routeUpdate.diffs,
              facts = routeUpdate.facts ++ extraFacts,
              impactedNodeIds
            )
          )
      }
    }
  }

  private def routeChangesRemoved(context: ChangeBuilderContext, routeIds: Set[Long]): Seq[RouteChange] = {

    val analysesBefore = routeAnalysesIn(context.networkBefore, routeIds)

    analysesBefore.flatMap { analysisBefore =>

      val routeId = analysisBefore.id

      context.routeAnalysesAfter.find(_.id == routeId) match {

        case None =>

          /*
              We cannot load the route from Overpass at the 'after' timestamp, we assume that
              the route is flagged as deleted in OpenStreetMap. If the RouteChange does not
              have a 'Deleted' fact yet (for example when the RouteChange was generated when
              the route was no longer referenced from a network), we add it now.
           */
          //noinspection SideEffectsInMonadicTransformation
          log.debug(s"OK: route '$routeId' has been deleted from the database.")

          val routeInfo = analysisBefore.route.copy(
            active = false,
            analysis = RouteInfoAnalysis(
              unexpectedNodeIds = Seq.empty,
              members = Seq.empty,
              expectedName = "",
              map = RouteMap(),
              structureStrings = Seq.empty,
              geometryDigest = "",
              locationAnalysis = RouteLocationAnalysis(None, Seq.empty, Seq.empty)
            ),
            lastUpdated = context.changeSetContext.changeSet.timestamp
          )


          routeRepository.save(routeInfo)
          tileChangeAnalyzer.analyzeRoute(analysisBefore)

          throw new IllegalStateException("Unexpected code execution - would need to calculate impactedNodeIds here")
          val impactedNodeIds: Seq[Long] = Seq.empty

          val key = context.changeSetContext.buildChangeKey(routeId)
          Some(
            RouteChangeStateAnalyzer.analyzed(
              RouteChange(
                _id = key.toId,
                key = key,
                changeType = ChangeType.Delete,
                name = analysisBefore.name,
                locationAnalysis = analysisBefore.route.analysis.locationAnalysis,
                addedToNetwork = Seq.empty,
                removedFromNetwork = context.networkBefore.map(_.toRef).toSeq,
                before = Some(analysisBefore.toRouteData),
                after = None,
                removedWays = Seq.empty,
                addedWays = Seq.empty,
                updatedWays = Seq.empty,
                diffs = RouteDiff(),
                facts = Seq(Fact.Deleted),
                impactedNodeIds
              )
            )
          )

        case Some(analysisAfter) =>
          processRemovedRoute(context, analysisBefore, analysisAfter)
      }
    }
  }

  private def processRemovedRoute(context: ChangeBuilderContext, analysisBefore: RouteAnalysis, analysisAfter: RouteAnalysis): Option[RouteChange] = {

    val routeId = analysisBefore.id
    val routeUpdate = new RouteDiffAnalyzer(analysisBefore, analysisAfter).analysis

    val facts = new RouteFactAnalyzer(analysisContext.data).facts(Some(analysisBefore), analysisAfter).filter(f => f == Fact.LostRouteTags)

    if (analysisContext.data.networks.isReferencingRelation(routeId)) {
      throw new IllegalStateException("Unexpected code execution - would need to calculate impactedNodeIds here")
      val impactedNodeIds: Seq[Long] = Seq.empty
      val key = context.changeSetContext.buildChangeKey(routeId)
      Some(
        RouteChangeStateAnalyzer.analyzed(
          RouteChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Update,
            name = analysisAfter.name,
            locationAnalysis = analysisAfter.route.analysis.locationAnalysis,
            addedToNetwork = Seq.empty,
            removedFromNetwork = context.networkBefore.map(_.toRef).toSeq,
            before = Some(analysisBefore.toRouteData),
            after = Some(analysisAfter.toRouteData),
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
    else {

      val elementIds = RelationAnalyzer.toElementIds(analysisAfter.relation)
      analysisContext.data.routes.watched.add(routeId, elementIds)

      routeRepository.save(analysisAfter.route.copy(/*orphan = true*/))

      //        analysisAfter.routeNodes.routeNodes.foreach { routeNode =>
      //          val country = countryAnalyzer.country(Seq(routeNode.node))
      //          val loadedNode = LoadedNode.from(country, routeNode.node.raw)
      //          val nodeInfo = NodeInfoBuilder.fromLoadedNode(loadedNode)
      //          analysisRepository.saveNode(nodeInfo)
      //        }

      val routeUpdate = new RouteDiffAnalyzer(analysisBefore, analysisAfter).analysis

      tileChangeAnalyzer.analyzeRouteChange(analysisBefore, analysisAfter)
      throw new IllegalStateException("Unexpected code execution - would need to calculate impactedNodeIds here")
      val impactedNodeIds: Seq[Long] = Seq.empty

      val key = context.changeSetContext.buildChangeKey(routeId)
      Some(
        RouteChangeStateAnalyzer.analyzed(
          RouteChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Update,
            name = analysisAfter.name,
            locationAnalysis = analysisAfter.route.analysis.locationAnalysis,
            addedToNetwork = Seq.empty,
            removedFromNetwork = context.networkBefore.map(_.toRef).toSeq,
            before = Some(analysisBefore.toRouteData),
            after = Some(analysisAfter.toRouteData),
            removedWays = routeUpdate.removedWays,
            addedWays = routeUpdate.addedWays,
            updatedWays = routeUpdate.updatedWays,
            diffs = routeUpdate.diffs,
            facts = facts :+ Fact.BecomeOrphan,
            impactedNodeIds
          )
        )
      )
    }
  }

  private def routeChangesUpdated(context: ChangeBuilderContext, routeIds: Set[Long]): Seq[RouteChange] = {

    val analysesBefore = routeAnalysesIn(context.networkBefore, routeIds)
    val analysesAfter = routeAnalysesIn(context.networkAfter, routeIds)

    throw new IllegalStateException("Unexpected code execution - would need to calculate impactedNodeIds here")
    val impactedNodeIds: Seq[Long] = Seq.empty

    analysesBefore.flatMap { analysisBefore =>

      val routeId = analysisBefore.id

      analysesAfter.find(_.id == routeId).flatMap { analysisAfter =>

        val routeUpdate = new RouteDiffAnalyzer(analysisBefore, analysisAfter).analysis

        if (routeUpdate.nonEmpty) {

          tileChangeAnalyzer.analyzeRouteChange(analysisBefore, analysisAfter)

          val key = context.changeSetContext.buildChangeKey(routeId)
          Some(
            RouteChangeStateAnalyzer.analyzed(
              RouteChange(
                _id = key.toId,
                key = key,
                changeType = ChangeType.Update,
                name = analysisAfter.name,
                locationAnalysis = analysisAfter.route.analysis.locationAnalysis,
                addedToNetwork = Seq.empty,
                removedFromNetwork = Seq.empty,
                before = Some(analysisBefore.toRouteData),
                after = Some(analysisAfter.toRouteData),
                removedWays = routeUpdate.removedWays,
                addedWays = routeUpdate.addedWays,
                updatedWays = routeUpdate.updatedWays,
                diffs = routeUpdate.diffs,
                facts = routeUpdate.facts,
                impactedNodeIds
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

  private def routeAnalysesIn(network: Option[Network], routeIds: Set[Long]): Seq[RouteAnalysis] = {
    network.toSeq.flatMap(_.routes.filter(route => routeIds.contains(route.id))).map(_.routeAnalysis)
  }
}
