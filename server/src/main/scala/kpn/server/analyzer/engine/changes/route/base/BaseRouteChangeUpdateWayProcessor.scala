package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Relation
import kpn.core.doc.Detail
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait BaseRouteChangeUpdateWayProcessor {

  def process(
    changeSetContext: ChangeSetContext,
    before: Detail,
    after: Detail,
    oldBeforeRelation: Relation,
    oldAfterRelation: Relation,
  ): ChangeSetContext
}
