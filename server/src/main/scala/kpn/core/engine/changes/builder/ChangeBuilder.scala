package kpn.core.engine.changes.builder

import kpn.core.analysis.Network
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

trait ChangeBuilder {
  def build(context: ChangeSetContext, networkBefore: Option[Network], networkAfter: Option[Network]): ChangeSetChanges
}
