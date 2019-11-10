package kpn.server.analyzer.engine.changes.orphan.route

import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.load.data.LoadedRoute

trait OrphanRouteProcessor {
  def process(context: ChangeSetContext, loadedRoute: LoadedRoute): Option[RouteAnalysis]
}
