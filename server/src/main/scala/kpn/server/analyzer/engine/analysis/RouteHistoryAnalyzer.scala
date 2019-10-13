package kpn.server.analyzer.engine.analysis

import kpn.server.analyzer.engine.changes.builder.RouteChangeInfoBuilder
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeSetInfo
import kpn.shared.changes.details.RouteChange
import kpn.shared.route.RouteChangeInfo
import kpn.shared.route.RouteChangeInfos

class RouteHistoryAnalyzer(routeChanges: Seq[RouteChange], changeSetInfos: Seq[ChangeSetInfo]) {

  def history: RouteChangeInfos = {
    val changes = routeChanges.map(toRouteChangeInfo)
    RouteChangeInfos(changes, incompleteWarning)
  }

  private def toRouteChangeInfo(routeChange: RouteChange): RouteChangeInfo = {
    new RouteChangeInfoBuilder().build(routeChange, changeSetInfos)
  }

  private def incompleteWarning: Boolean = {
    if (routeChanges.isEmpty) {
      false
    }
    else {
      routeChanges.last.before match {
        case None => false
        case Some(lastRouteChange) =>
          lastRouteChange.relation.version > 1 &&
            lastRouteChange.relation.timestamp < Timestamp.redaction
      }
    }
  }
}
