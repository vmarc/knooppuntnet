package kpn.server.analyzer.engine.changes.route.base

import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeDeleteProcessor(
  baseRouteDeleter: BaseRouteChangeDeleter,
) extends BaseRouteChangeSubProcessor {

  def process(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    baseRouteDeleter.delete(changeSetContext, routeId)
  }
}
