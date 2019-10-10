package kpn.core.engine.changes.network.create

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

trait NetworkCreateProcessor {
  def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges]
}
