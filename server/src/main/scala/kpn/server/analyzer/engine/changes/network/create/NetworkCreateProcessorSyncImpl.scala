package kpn.server.analyzer.engine.changes.network.create

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

import scala.concurrent.Future

class NetworkCreateProcessorSyncImpl(
  worker: NetworkCreateProcessorWorker
) extends NetworkCreateProcessor {

  override def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges] = {
    Future.successful(worker.process(context, networkId))
  }
}
