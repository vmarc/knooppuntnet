package kpn.server.analyzer.engine.changes.route.base

import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait BaseRouteChangeProcessor extends ChangeProcessor {
  def process(changeSetContext: ChangeSetContext): ChangeSetContext
}
