package kpn.core

import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.ElementIds

trait TestObjects extends SharedTestObjects {

  def newChangeSetContext(): ChangeSetContext = {
    ChangeSetContext(ReplicationId(1), newChangeSet(), ElementIds())
  }
}
