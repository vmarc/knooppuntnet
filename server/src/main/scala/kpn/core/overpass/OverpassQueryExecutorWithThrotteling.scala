package kpn.core.overpass

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.routing.BalancingPool
import akka.util.Timeout
import kpn.core.util.Log

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

case class OverpassQueryWithContext(contextMessages: Seq[String], queryString: String)

class OverpassActor(executor: OverpassQueryExecutor) extends Actor {
  def receive: Actor.Receive = {
    case query: OverpassQueryWithContext =>
      sender() ! {
        Log.context(query.contextMessages) {
          executor.execute(query.queryString)
        }
      }
  }
}

class OverpassQueryExecutorWithThrotteling(system: ActorSystem, executor: OverpassQueryExecutor) extends OverpassQueryExecutor {

  private val router = system.actorOf(BalancingPool(3).props(Props(classOf[OverpassActor], executor)), "overpass-query")

  def execute(queryString: String): String = {
    val future = router.ask(OverpassQueryWithContext(Log.contextMessages, queryString))(Timeout(1000.second))
    Await.result(future, Duration.Inf).toString
  }
}
