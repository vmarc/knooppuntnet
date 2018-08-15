package kpn.core.load

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.routing.BalancingPool
import akka.util.Timeout
import kpn.core.util.Log

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration._

class Pool[P, R](system: ActorSystem, poolName: String)(worker: P => Future[R]) {

  private implicit val askTimeout: Timeout = Timeout(4.hours)
  private implicit val executionContext: ExecutionContext = system.dispatcher

  private case class Param(messages: Seq[String], param: P)

  private class WorkerActor() extends Actor {
    def receive: Actor.Receive = {
      case msg: Param =>
        Log.context(msg.messages) {
          sender() ! worker(msg.param)
        }
    }
  }

  private val workerPool = {
    val props = Props(classOf[WorkerActor], this)
    system.actorOf(BalancingPool(3).props(props), poolName)
  }

  def execute(msg: P): Future[R] = {
    ask(workerPool, Param(Log.contextMessages, msg)).mapTo[Future[R]].flatMap(identity)
  }
}
