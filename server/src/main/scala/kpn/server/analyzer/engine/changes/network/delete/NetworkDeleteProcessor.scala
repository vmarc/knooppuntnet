package kpn.server.analyzer.engine.changes.network.delete

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

trait NetworkDeleteProcessor {
  def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges]
}
