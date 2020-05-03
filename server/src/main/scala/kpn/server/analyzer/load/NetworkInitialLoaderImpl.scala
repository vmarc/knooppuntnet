package kpn.server.analyzer.load

import java.util.concurrent.CompletableFuture.allOf
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.Executor

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class NetworkInitialLoaderImpl(
  analysisExecutor: Executor,
  worker: NetworkInitialLoaderWorker
) extends NetworkInitialLoader {

  private val log = Log(classOf[NetworkInitialLoaderWorkerImpl])

  def load(timestamp: Timestamp, networkIds: Seq[Long]): Unit = {
    val futures = networkIds.zipWithIndex.map { case (networkId, index) =>
      val context = Log.contextAnd(s"${index + 1}/${networkIds.size}, network=$networkId")
      supplyAsync(() => Log.context(context)(worker.load(timestamp, networkId)), analysisExecutor).exceptionally { ex =>
        val message = "Exception during initial network load"
        Log.context(context) {
          log.error(message, ex)
        }
        throw new RuntimeException(s"[$context] $message", ex)
      }
    }
    allOf(futures: _*).join()
  }
}
