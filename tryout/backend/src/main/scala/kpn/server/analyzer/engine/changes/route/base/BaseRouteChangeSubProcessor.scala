package kpn.server.analyzer.engine.changes.route.base

import kpn.server.analyzer.engine.changes.ChangeSetContext

trait BaseRouteChangeSubProcessor {
  def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext
}
