package kpn.server.analyzer.engine.changes.network.delete

import java.util.concurrent.CompletableFuture

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

class NetworkDeleteProcessorSyncImpl(
  worker: NetworkDeleteProcessorWorker
) extends NetworkDeleteProcessor {
  override def process(context: ChangeSetContext, networkId: Long): CompletableFuture[ChangeSetChanges] = {
    CompletableFuture.completedFuture(worker.process(context, networkId))
  }
}
