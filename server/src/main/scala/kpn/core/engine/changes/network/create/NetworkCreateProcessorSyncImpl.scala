package kpn.core.engine.changes.network.create

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

class NetworkCreateProcessorSyncImpl(
  worker: NetworkCreateProcessorWorker
) extends NetworkCreateProcessor {

  override def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges] = {
    Future.successful(worker.process(context, networkId))
  }
}
