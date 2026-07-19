package kpn.server.analyzer.engine.changes.route.base

import kpn.server.analyzer.engine.changes.ChangeSetContext

trait BaseRouteChangeDeleter {
  def delete(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext
}
