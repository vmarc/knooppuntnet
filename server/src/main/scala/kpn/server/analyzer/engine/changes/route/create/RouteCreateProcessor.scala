package kpn.server.analyzer.engine.changes.route.create

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

trait RouteCreateProcessor {
  def process(context: ChangeSetContext, routeId: Long): Future[ChangeSetChanges]
}
