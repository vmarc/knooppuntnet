package kpn.server.analyzer.engine.changes.route.update

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import org.springframework.stereotype.Component

import scala.concurrent.Future

@Component
class RouteUpdateProcessorImpl extends RouteUpdateProcessor {
  override def process(context: ChangeSetContext, routeId: Long): Future[ChangeSetChanges] = {
    null
  }
}
