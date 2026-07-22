package kpn.server.analyzer.engine.changes.builder

import kpn.core.analysis.Network
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

trait ChangeBuilder {
  def build(context: ChangeSetContext, networkBefore: Option[Network], networkAfter: Option[Network]): ChangeSetChanges
}
