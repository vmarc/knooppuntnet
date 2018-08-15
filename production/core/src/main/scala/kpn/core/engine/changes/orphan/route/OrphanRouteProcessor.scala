package kpn.core.engine.changes.orphan.route

import kpn.core.engine.analysis.route.RouteAnalysis
import kpn.core.load.data.LoadedRoute
import kpn.core.engine.changes.ChangeSetContext

trait OrphanRouteProcessor {
  def process(context: ChangeSetContext, loadedRoute: LoadedRoute): Option[RouteAnalysis]
}
