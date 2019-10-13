package kpn.server.analyzer.engine.changes.network.update

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

trait NetworkUpdateProcessor {
  def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges]
}
