package kpn.core.engine.changes.network.delete

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.routing.BalancingPool
import akka.util.Timeout
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.core.util.Log

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration._

class NetworkDeleteProcessorImpl(
  system: ActorSystem,
  worker: NetworkDeleteProcessorWorker
) extends NetworkDeleteProcessor {

  private implicit val askTimeout: Timeout = Timeout(1000.second)
  private implicit val executionContext: ExecutionContext = system.dispatcher

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
    system.actorOf(BalancingPool(3).props(props), "network-delete-processor")
  }

  override def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges] = {
    ask(workerPool, Process(Log.contextMessages, context, networkId)).mapTo[ChangeSetChanges]
  }
}
