package kpn.server.analyzer.engine.changes.route.main

import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait RouteChangeProcessor extends ChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetContext
}
