package kpn.server.analyzer.load

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.routing.BalancingPool
import akka.util.Timeout
import kpn.server.analyzer.load.data.LoadedRoute
import kpn.core.util.Log
import kpn.shared.Timestamp
import org.springframework.stereotype.Component

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

@Component
class RoutesLoaderImpl(
  system: ActorSystem,
  routeLoader: RouteLoader
) extends RoutesLoader {

  private implicit val askTimeout: Timeout = Timeout(2.hours)
  private implicit val executionContext: ExecutionContext = system.dispatcher

  private val log = Log(classOf[RoutesLoaderImpl])

  case class Load(messages: Seq[String], timestamp: Timestamp, routeId: Long)

  class WorkerActor extends Actor {
    def receive: Actor.Receive = {
      case Load(messages, timestamp, routeId) =>
        Log.context(messages) {
          sender() ! routeLoader.loadRoute(timestamp, routeId)
        }
    }
  }

  private val workerPool = {
    val props = Props(classOf[WorkerActor], this)
    system.actorOf(BalancingPool(3).props(props), "routes-loader")
  }

  override def load(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Option[LoadedRoute]] = {
    if (routeIds.isEmpty) {
      Seq()
    }
    else {
      log.debugElapsed {
        val futures = routeIds.zipWithIndex.map { case (routeId, index) =>
          Log.context(s"${index + 1}/${routeIds.size}") {
            Log.context(s"route=$routeId") {
              ask(workerPool, Load(Log.contextMessages, timestamp, routeId)).mapTo[Option[LoadedRoute]]
            }
          }
        }
        val loadedRoutes = Await.result(Future.sequence(futures), Duration.Inf)
        (s"Processed ${routeIds.size} routes", loadedRoutes)
      }
    }
  }
}
