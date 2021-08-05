package kpn.server.analyzer.engine.changes.route.update

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

trait RouteUpdateProcessor {
  def process(context: ChangeSetContext, routeId: Long): Future[ChangeSetChanges]
}
