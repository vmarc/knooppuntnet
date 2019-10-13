package kpn.server.analyzer.engine.changes.network.delete

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

class NetworkDeleteProcessorSyncImpl(
  worker: NetworkDeleteProcessorWorker
) extends NetworkDeleteProcessor {
  override def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges] = {
    Future.successful(worker.process(context, networkId))
  }
}
