package kpn.core.engine.changes.builder

import kpn.core.analysis.Network
import kpn.core.changes.RelationAnalyzer
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.route.MasterRouteAnalyzer
import kpn.core.engine.analysis.route.RouteAnalysis
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.route.RouteFactAnalyzer
import kpn.core.engine.changes.route.RouteUtil
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.load.RouteLoader
import kpn.core.repository.AnalysisRepository
import kpn.core.util.Log
import kpn.shared.Fact
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.RouteChange
import kpn.shared.diff.route.RouteDiff

class RouteChangeBuilderImpl(
  analysisData: AnalysisData,
  analysisRepository: AnalysisRepository,
  countryAnalyzer: CountryAnalyzer,
  routeAnalyzer: MasterRouteAnalyzer,
  routeLoader: RouteLoader
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
        if (analysisData.orphanRoutes.watched.contains(routeId)) {
          analysisData.orphanRoutes.watched.delete(routeId)
          Seq(Fact.WasOrphan)
        }
        else {
          Seq()
        },
        if (analysisData.orphanRoutes.ignored.contains(routeId)) {
          analysisData.orphanRoutes.ignored.delete(routeId)
          Seq(Fact.WasIgnored)
        }
        else {
          Seq()
        }
      ).flatten

      context.routeAnalysesBefore.find(_.id == routeId) match {

        case None =>

          /*
            Cannot find 'before' in database; this must be version 1 of the route.
           */
          RouteUtil.assertVersion1(analysisAfter)

          RouteChange(
            key = context.changeSetContext.buildChangeKey(analysisAfter.id),
            changeType = ChangeType.Create,
            name = analysisAfter.name,
            addedToNetwork = context.networkAfter.map(_.toRef).toSeq,
            removedFromNetwork = Seq.empty,
            before = None,
            after = Some(analysisAfter.toRouteData),
            removedWays = Seq.empty,
            addedWays = Seq.empty,
            updatedWays = Seq.empty,
            diffs = RouteDiff(),
            facts = extraFacts
          )

        case Some(analysisBefore) =>

          val routeUpdate = new RouteDiffAnalyzer(analysisBefore, analysisAfter).analysis

          RouteChange(
            key = context.changeSetContext.buildChangeKey(routeId),
            changeType = ChangeType.Update,
            name = analysisAfter.name,
            addedToNetwork = context.networkAfter.map(_.toRef).toSeq,
            removedFromNetwork = Seq.empty,
            before = Some(analysisBefore.toRouteData),
            after = Some(analysisAfter.toRouteData),
            removedWays = routeUpdate.removedWays,
            addedWays = routeUpdate.addedWays,
            updatedWays = routeUpdate.updatedWays,
            diffs = routeUpdate.diffs,
            facts = routeUpdate.facts ++ extraFacts
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
            analysis = None,
            lastUpdated = context.changeSetContext.changeSet.timestamp
          )

          analysisRepository.saveRoute(routeInfo)

          Some(
            RouteChange(
              key = context.changeSetContext.buildChangeKey(routeId),
              changeType = ChangeType.Delete,
              name = analysisBefore.name,
              addedToNetwork = Seq.empty,
              removedFromNetwork = context.networkBefore.map(_.toRef).toSeq,
              before = Some(analysisBefore.toRouteData),
              after = None,
              removedWays = Seq.empty,
              addedWays = Seq.empty,
              updatedWays = Seq.empty,
              diffs = RouteDiff(),
              facts = Seq(Fact.Deleted)
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

    val facts = new RouteFactAnalyzer(analysisData).facts(Some(analysisBefore), analysisAfter).filter(f => f == Fact.LostRouteTags)

    if (analysisData.networks.isReferencingRelation(routeId)) {
      Some(
        RouteChange(
          key = context.changeSetContext.buildChangeKey(routeId),
          changeType = ChangeType.Update,
          name = analysisAfter.name,
          addedToNetwork = Seq.empty,
          removedFromNetwork = context.networkBefore.map(_.toRef).toSeq,
          before = Some(analysisBefore.toRouteData),
          after = Some(analysisAfter.toRouteData),
          removedWays = routeUpdate.removedWays,
          addedWays = routeUpdate.addedWays,
          updatedWays = routeUpdate.updatedWays,
          diffs = routeUpdate.diffs,
          facts = facts
        )
      )
    }
    else {

      if (analysisAfter.route.ignored) {

        val elementIds = RelationAnalyzer.toElementIds(analysisAfter.relation)
        analysisData.orphanRoutes.ignored.add(routeId, elementIds)

        analysisRepository.saveRoute(analysisAfter.route.copy(orphan = true))

        Some(
          RouteChange(
            key = context.changeSetContext.buildChangeKey(routeId),
            changeType = ChangeType.Update,
            name = analysisAfter.name,
            addedToNetwork = Seq.empty,
            removedFromNetwork = context.networkBefore.map(_.toRef).toSeq,
            before = Some(analysisBefore.toRouteData),
            after = Some(analysisAfter.toRouteData),
            removedWays = routeUpdate.removedWays,
            addedWays = routeUpdate.addedWays,
            updatedWays = routeUpdate.updatedWays,
            diffs = routeUpdate.diffs,
            facts = facts :+ Fact.BecomeIgnored
          )
        )
      }
      else {

        val elementIds = RelationAnalyzer.toElementIds(analysisAfter.relation)
        analysisData.orphanRoutes.watched.add(routeId, elementIds)

        analysisRepository.saveRoute(analysisAfter.route.copy(orphan = true))

        //        analysisAfter.routeNodes.routeNodes.foreach { routeNode =>
        //          val country = countryAnalyzer.country(Seq(routeNode.node))
        //          val loadedNode = LoadedNode.from(country, routeNode.node.raw)
        //          val nodeInfo = NodeInfoBuilder.fromLoadedNode(loadedNode)
        //          analysisRepository.saveNode(nodeInfo)
        //        }

        val routeUpdate = new RouteDiffAnalyzer(analysisBefore, analysisAfter).analysis

        Some(
          RouteChange(
            key = context.changeSetContext.buildChangeKey(routeId),
            changeType = ChangeType.Update,
            name = analysisAfter.name,
            addedToNetwork = Seq.empty,
            removedFromNetwork = context.networkBefore.map(_.toRef).toSeq,
            before = Some(analysisBefore.toRouteData),
            after = Some(analysisAfter.toRouteData),
            removedWays = routeUpdate.removedWays,
            addedWays = routeUpdate.addedWays,
            updatedWays = routeUpdate.updatedWays,
            diffs = routeUpdate.diffs,
            facts = facts :+ Fact.BecomeOrphan
          )
        )
      }
    }
  }

  private def routeChangesUpdated(context: ChangeBuilderContext, routeIds: Set[Long]): Seq[RouteChange] = {

    val analysesBefore = routeAnalysesIn(context.networkBefore, routeIds)
    val analysesAfter = routeAnalysesIn(context.networkAfter, routeIds)

    analysesBefore.flatMap { analysisBefore =>

      val routeId = analysisBefore.id

      analysesAfter.find(_.id == routeId).flatMap { analysisAfter =>

        val routeUpdate = new RouteDiffAnalyzer(analysisBefore, analysisAfter).analysis

        if (routeUpdate.nonEmpty) {
          Some(
            RouteChange(
              key = context.changeSetContext.buildChangeKey(routeId),
              changeType = ChangeType.Update,
              name = analysisAfter.name,
              addedToNetwork = Seq.empty,
              removedFromNetwork = Seq.empty,
              before = Some(analysisBefore.toRouteData),
              after = Some(analysisAfter.toRouteData),
              removedWays = routeUpdate.removedWays,
              addedWays = routeUpdate.addedWays,
              updatedWays = routeUpdate.updatedWays,
              diffs = routeUpdate.diffs,
              facts = routeUpdate.facts
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
