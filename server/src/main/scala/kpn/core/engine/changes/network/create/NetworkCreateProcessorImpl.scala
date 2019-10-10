package kpn.core.engine.changes.network.create

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

class NetworkCreateProcessorImpl(
  system: ActorSystem,
  worker: NetworkCreateProcessorWorker
) extends NetworkCreateProcessor {

  /*
   Processing a network create can take very long when the new network relation contains
   a lot of already existing nodes and routes, because the logic will try to load all the nodes
   and routes at the 'before' timestamp one by one. Example: network 6483947 in changeset 002/048/328.
  */
  private implicit val askTimeout: Timeout = Timeout(30.minutes)
  private implicit val executionContext: ExecutionContext = system.dispatcher

  case class Load(messages: Seq[String], context: ChangeSetContext, networkId: Long)

  class WorkerActor extends Actor {
    def receive: Actor.Receive = {
      case Load(messages, changeSetContext, networkId) =>
        Log.context(messages) {
          sender() ! worker.process(changeSetContext, networkId)
        }
    }
  }

  private val workerPool = {
    val props = Props(classOf[WorkerActor], this)
    system.actorOf(BalancingPool(3).props(props), "network-create-processor")
  }

  override def process(context: ChangeSetContext, networkId: Long): Future[ChangeSetChanges] = {
    ask(workerPool, Load(Log.contextMessages, context, networkId)).mapTo[ChangeSetChanges]
  }
}
