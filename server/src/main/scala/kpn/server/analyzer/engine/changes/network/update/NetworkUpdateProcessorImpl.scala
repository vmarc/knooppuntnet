package kpn.server.analyzer.engine.changes.network.update

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.routing.BalancingPool
import akka.util.Timeout
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import org.springframework.stereotype.Component

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration._

@Component
class NetworkUpdateProcessorImpl(
  system: ActorSystem,
  worker: NetworkUpdateProcessorWorker
) extends NetworkUpdateProcessor {

  implicit val askTimeout: Timeout = Timeout(1000.second)
  implicit val executionContext: ExecutionContext = system.dispatcher

  case class Process(messages: Seq[String], context: ChangeSetContext, networkId: Long)

  class WorkerActor extends Actor {
    def receive: Actor.Receive = {
      case Process(messages, changeSetContext, networkId) =>
        Log.context(messages) {
          sender() ! worker.process(changeSetContext, networkId)
        }
    }
  }

  private val workerPool = {
    val props = Props(classOf[WorkerActor], this)
    system.actorOf(BalancingPool(3).props(props), "network-update-processor")
  }

  override def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges] = {
    ask(workerPool, Process(Log.contextMessages, context, networkId)).mapTo[ChangeSetChanges]
  }
}
