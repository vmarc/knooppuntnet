package kpn.core.engine.changes.network.delete

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

trait NetworkDeleteProcessor {
  def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges]
}
