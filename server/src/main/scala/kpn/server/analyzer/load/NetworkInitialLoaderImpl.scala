package kpn.server.analyzer.load

import akka.actor.ActorSystem
import kpn.core.util.Log
import kpn.shared.Timestamp
import org.springframework.stereotype.Component

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration._

@Component
class NetworkInitialLoaderImpl(
  system: ActorSystem,
  worker: NetworkInitialLoaderWorker
) extends NetworkInitialLoader {

  val pool = new Pool[NetworkInitialLoad, Unit](system, "network-initial-loader")(worker.load)

  def load(timestamp: Timestamp, networkIds: Seq[Long]): Unit = {
    implicit val executionContext: ExecutionContext = system.dispatcher
    val futures = networkIds.zipWithIndex.map { case (networkId, index) =>
      Log.context(s"${index + 1}/${networkIds.size}") {
        pool.execute(NetworkInitialLoad(timestamp, networkId))
      }
    }
    Await.result(Future.sequence(futures), Duration.Inf)
  }
}
