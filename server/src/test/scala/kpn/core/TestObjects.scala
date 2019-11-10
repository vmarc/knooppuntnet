package kpn.core

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects

trait TestObjects extends SharedTestObjects {

  def newChangeSetContext(): ChangeSetContext = {
    ChangeSetContext(ReplicationId(1), newChangeSet())
  }
}
