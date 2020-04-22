package kpn.server.analyzer.engine.changes.network.create

import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.Executor

import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import org.springframework.stereotype.Component

@Component
class NetworkCreateProcessorImpl(
  analysisExecutor: Executor,
  worker: NetworkCreateProcessorWorker
) extends NetworkCreateProcessor {

  def log: Log = Log(classOf[NetworkCreateProcessorImpl])

  override def process(changeSetContext: ChangeSetContext, networkId: Long): CompletableFuture[ChangeSetChanges] = {
    val context = s"network=$networkId"
    supplyAsync(() => Log.context(context)(worker.process(changeSetContext, networkId)), analysisExecutor).exceptionally { ex =>
      val message = "Exception while processing network create"
      Log.context(context) {
        log.error(message, ex)
        throw new RuntimeException(s"${Log.contextString} $message")
      }
    }
  }
}
