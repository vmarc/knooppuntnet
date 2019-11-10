package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.route.RouteChangeInfo
import kpn.api.common.route.RouteChangeInfos
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.builder.RouteChangeInfoBuilder

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
