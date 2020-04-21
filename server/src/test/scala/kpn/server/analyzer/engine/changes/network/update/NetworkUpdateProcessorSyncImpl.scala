package kpn.server.analyzer.engine.changes.network.update

import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.completedFuture

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

class NetworkUpdateProcessorSyncImpl(
  worker: NetworkUpdateProcessorWorker
) extends NetworkUpdateProcessor {

  override def process(context: ChangeSetContext, networkId: Long): CompletableFuture[ChangeSetChanges] = {
    completedFuture(worker.process(context, networkId))
  }
}
