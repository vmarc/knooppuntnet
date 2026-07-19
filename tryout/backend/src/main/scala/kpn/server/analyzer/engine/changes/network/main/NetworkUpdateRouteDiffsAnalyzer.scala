package kpn.server.analyzer.engine.changes.network.main

import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

object NetworkUpdateRouteDiffsAnalyzer {

  def analyze(context: ChangeSetContext, before: NetworkDoc, after: NetworkDoc): RefDiffs = {

    val (routeIdsAdded, routeIdsRemoved, routeIdsCommon) = analyzeRouteDiffs(before, after)

    val routeRefsAdded = toRouteRefs(after, routeIdsAdded)
    val routeRefsRemoved = toRouteRefs(before, routeIdsRemoved)
    val routeRefsUpdated = findUpdatedRouteRefs(context, before, after, routeIdsCommon, before._id)

    RefDiffs(
      removed = routeRefsRemoved,
      added = routeRefsAdded,
      updated = routeRefsUpdated
    )
  }

  private def analyzeRouteDiffs(before: NetworkDoc, after: NetworkDoc): (Set[Long], Set[Long], Set[Long]) = {
    val routeIdsBefore = before.routes.map(_.id).toSet
    val routeIdsAfter = after.routes.map(_.id).toSet

    val added = routeIdsAfter -- routeIdsBefore
    val removed = routeIdsBefore -- routeIdsAfter
    val common = routeIdsBefore.intersect(routeIdsAfter)

    (added, removed, common)
  }

  private def findUpdatedRouteRefs(
    context: ChangeSetContext,
    before: NetworkDoc,
    after: NetworkDoc,
    commonIds: Set[Long],
    networkId: Long
  ): Seq[Ref] = {
    val relevantChanges = context.changes.routeChanges.filter { change =>
      val baseRouteChangeOption = context.changes.baseRouteChanges.find(_.routeId == change.id)
      isRelevant(change, baseRouteChangeOption, networkId)
    }
    commonIds.toSeq.sorted.flatMap { routeId =>
      if (relevantChanges.exists(_.id == routeId)) {
        findRouteRef(after, routeId)
      } else {
        findChangedRouteRef(before, after, routeId)
      }
    }
  }

  private def findRouteRef(networkDoc: NetworkDoc, routeId: Long): Option[Ref] = {
    networkDoc.routes.find(_.id == routeId).map(route => Ref(routeId, route.name))
  }

  private def findChangedRouteRef(before: NetworkDoc, after: NetworkDoc, routeId: Long): Option[Ref] = {
    for {
      routeBefore <- before.routes.find(_.id == routeId)
      routeAfter <- after.routes.find(_.id == routeId)
      if !routeBefore.isSameAs(routeAfter)
    } yield Ref(routeId, routeAfter.name)
  }

  private def toRouteRefs(networkDoc: NetworkDoc, routeIds: Set[Long]): Seq[Ref] = {
    networkDoc.routes
      .filter(route => routeIds.contains(route.id))
      .map(_.toRef)
      .sortBy(_.id)
  }

  private def isRelevant(routeChange: RouteChange, baseRouteChange: Option[BaseRouteChange], networkId: Long): Boolean = {
    val diffsChanged = baseRouteChange.toSeq.exists(_.routeDiff.nonEmpty)
    val factsChanged = routeChange.facts.nonEmpty
    networkChanged(routeChange, networkId) ||
      routeChanged(routeChange) ||
      waysChanged(baseRouteChange) ||
      diffsChanged ||
      factsChanged
  }

  private def networkChanged(routeChange: RouteChange, networkId: Long): Boolean = {
    routeChange.addedToNetwork.map(_.id).contains(networkId) ||
      routeChange.removedFromNetwork.map(_.id).contains(networkId)
  }

  private def routeChanged(routeChange: RouteChange): Boolean = {
    routeChange.before != routeChange.after
  }

  private def waysChanged(baseRouteChangeOption: Option[BaseRouteChange]): Boolean = {
    baseRouteChangeOption.exists(_.wayDiffs.nonEmpty)
  }
}
