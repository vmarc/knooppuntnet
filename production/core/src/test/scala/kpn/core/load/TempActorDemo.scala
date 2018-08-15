package kpn.core.load

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.io.IO
import akka.pattern.ask
import akka.routing.BalancingPool
import akka.util.Timeout
import kpn.core.app.ActorSystemConfig
import spray.can.Http

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt
import scala.util.Random

case class DemoMessage(string: String)

case class Completed(string: String)

class DemoActor extends Actor {
  def receive: Actor.Receive = {
    case DemoMessage(string) =>
      println("thread=" + Thread.currentThread().getName)
      Thread.sleep(Random.nextInt(50).toLong)
      sender() ! Completed(string + " completed")
  }
}

object TempActorDemo extends App {

  val system = ActorSystemConfig.actorSystem()
  try {
    new TempActorDemo(system).start()
  }
  finally {
    Await.ready(IO(Http)(system).ask(Http.CloseAll)(15.seconds), 15.seconds)
    Await.result(system.terminate(), Duration.Inf)
  }
}

class TempActorDemo(system: ActorSystem) {

  private val demoActor = {
    val props = Props(classOf[DemoActor])
    system.actorOf(BalancingPool(3).props(props), "demo")
  }

  def start(): Unit = {
    implicit val askTimeout = Timeout(1000.second)
    implicit val executionContext = system.dispatcher
    val futures = (1 to 100).map { id =>
      demoActor ? DemoMessage("message " + id)
    }

    val results = Await.result(Future.sequence(futures), Duration.Inf)

    results.foreach(println)
  }
}
