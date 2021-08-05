package kpn.server.analyzer.engine.changes.route.delete

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

trait RouteDeleteProcessor {
  def process(context: ChangeSetContext, routeId: Long): Future[ChangeSetChanges]
}
