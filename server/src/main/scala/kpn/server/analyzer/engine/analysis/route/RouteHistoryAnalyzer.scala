package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.route.RouteChangeInfo
import kpn.api.common.route.RouteChangeInfos
import kpn.server.analyzer.engine.changes.builder.RouteChangeInfoBuilder

class RouteHistoryAnalyzer(routeChanges: Seq[RouteChange], changeSetInfos: Seq[ChangeSetInfo]) {

  def history: RouteChangeInfos = {
    val changes = routeChanges.zipWithIndex.map { case (routeChange, index) =>
      toRouteChangeInfo(index, routeChange)
    }
    RouteChangeInfos(changes)
  }

  private def toRouteChangeInfo(index: Int, routeChange: RouteChange): RouteChangeInfo = {
    new RouteChangeInfoBuilder().build(index, routeChange, changeSetInfos)
  }
}
