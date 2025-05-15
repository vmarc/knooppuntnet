package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.common.Ref
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

object NetworkInfoUpdateRouteDiffsAnalyzer {

  def analyze(context: ChangeSetContext, before: NetworkDoc, after: NetworkDoc): RefDiffs = {
    val routeIdsBefore = before.routes.map(_.id).toSet
    val routeIdsAfter = after.routes.map(_.id).toSet
    val routeIdsAdded = routeIdsAfter -- routeIdsBefore
    val routeIdsRemoved = routeIdsBefore -- routeIdsAfter
    val routeIdsCommon = routeIdsBefore.intersect(routeIdsAfter)
    val routeRefsAdded = after.routes.filter(route => routeIdsAdded.contains(route.id)).map(_.toRef).sortBy(_.id)
    val routeRefsRemoved = before.routes.filter(route => routeIdsRemoved.contains(route.id)).map(_.toRef).sortBy(_.id)

    val networkId = before._id
    val relevantRouteChanges = context.changes.routeChanges.filter { routeChange =>
      routeChange.addedToNetwork.map(_.id).contains(networkId) ||
        routeChange.removedFromNetwork.map(_.id).contains(networkId) ||
        routeChange.before != routeChange.after ||
        routeChange.removedWays.nonEmpty ||
        routeChange.addedWays.nonEmpty ||
        routeChange.updatedWays.nonEmpty ||
        routeChange.diffs.nonEmpty ||
        routeChange.facts.nonEmpty
    }

    val routeRefsUpdated = routeIdsCommon.toSeq.sorted.flatMap { routeId =>

      if (relevantRouteChanges.exists(_.id == routeId)) {
        after.routes.find(_.id == routeId).map { routeAfter =>
          Ref(routeId, routeAfter.name)
        }
      }
      else {
        before.routes.find(route => route.id == routeId).flatMap { routeBefore =>
          after.routes.find(route => route.id == routeId).flatMap { routeAfter =>
            Option.when(!routeBefore.isSameAs(routeAfter)) {
              Ref(routeId, routeAfter.name)
            }
          }
        }
      }
    }
    RefDiffs(
      removed = routeRefsRemoved,
      added = routeRefsAdded,
      updated = routeRefsUpdated
    )
  }
}
