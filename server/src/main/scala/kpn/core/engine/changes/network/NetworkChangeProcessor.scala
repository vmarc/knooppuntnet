package kpn.core.engine.changes.network

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

trait NetworkChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetChanges
}
