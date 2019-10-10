package kpn.core.engine.changes.network.delete

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

class NetworkDeleteProcessorSyncImpl(
  worker: NetworkDeleteProcessorWorker
) extends NetworkDeleteProcessor {
  override def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges] = {
    Future.successful(worker.process(context, networkId))
  }
}
