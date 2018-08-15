package kpn.core.engine.changes.network.update

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

trait NetworkUpdateProcessor {
  def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges]
}
