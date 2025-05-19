package kpn.server.analyzer.engine.changes.route.base

import kpn.api.custom.Relation
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait BaseRouteChangeUpdateWayProcessor {

  def process(
    changeSetContext: ChangeSetContext,
    before: Relation,
    after: Relation,
  ): ChangeSetContext
}
