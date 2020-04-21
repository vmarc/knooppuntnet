package kpn.server.analyzer.engine.changes.network.delete

import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.Executor

import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import org.springframework.stereotype.Component

@Component
class NetworkDeleteProcessorImpl(
  executor: Executor,
  worker: NetworkDeleteProcessorWorker
) extends NetworkDeleteProcessor {

  private val log = Log(classOf[NetworkDeleteProcessorImpl])

  override def process(changeSetContext: ChangeSetContext, networkId: Long): CompletableFuture[ChangeSetChanges] = {
    val context = Log.contextAnd(s"network=$networkId")
    supplyAsync(() => Log.context(context)(worker.process(changeSetContext, networkId)), executor).exceptionally { ex =>
      val message = "Exception while processing network delete"
      Log.context(context) {
        log.error(message, ex)
        throw new RuntimeException(s"${Log.contextString} $message")
      }
    }
  }
}
