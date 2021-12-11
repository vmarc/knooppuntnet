package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.common.Ref
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkInfoDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

object NetworkInfoUpdateRouteDiffsAnalyzer {

  def analyze(context: ChangeSetContext, before: NetworkInfoDoc, after: NetworkInfoDoc): RefDiffs = {
    val routeIdsBefore = before.routes.map(_.id).toSet
    val routeIdsAfter = after.routes.map(_.id).toSet
    val routeIdsAdded = routeIdsAfter -- routeIdsBefore
    val routeIdsRemoved = routeIdsBefore -- routeIdsAfter
    val routeIdsCommon = routeIdsBefore.intersect(routeIdsAfter)
    val routeRefsAdded = after.routes.filter(route => routeIdsAdded.contains(route.id)).map(_.toRef).sortBy(_.id)
    val routeRefsRemoved = before.routes.filter(route => routeIdsRemoved.contains(route.id)).map(_.toRef).sortBy(_.id)
    val routeRefsUpdated = routeIdsCommon.toSeq.sorted.flatMap { routeId =>
      if (context.changes.routeChanges.exists(_.id == routeId)) {
        after.routes.find(_.id == routeId).map { routeAfter =>
          Ref(routeId, routeAfter.name)
        }
      }
      else {
        before.routes.find(route => route.id == routeId).flatMap { routeBefore =>
          after.routes.find(route => route.id == routeId).flatMap { routeAfter =>
            if (!routeBefore.isSameAs(routeAfter)) {
              Some(Ref(routeId, routeAfter.name))
            }
            else {
              None
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
