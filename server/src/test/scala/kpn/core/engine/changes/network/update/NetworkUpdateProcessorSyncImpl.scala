package kpn.core.engine.changes.network.update

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

class NetworkUpdateProcessorSyncImpl(
  worker: NetworkUpdateProcessorWorker
) extends NetworkUpdateProcessor {

  override def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges] = {
    Future.successful(worker.process(context, networkId))
  }
}
