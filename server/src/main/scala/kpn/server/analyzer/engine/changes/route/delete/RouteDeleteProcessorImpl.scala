package kpn.server.analyzer.engine.changes.route.delete

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import org.springframework.stereotype.Component

import scala.concurrent.Future

@Component
class RouteDeleteProcessorImpl extends RouteDeleteProcessor {
  override def process(context: ChangeSetContext, routeId: Long): Future[ChangeSetChanges] = {
    null
  }
}
