package kpn.core.load

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import kpn.core.app.ActorSystemConfig
import kpn.core.engine.analysis.country.CountryAnalyzerImpl
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorWithThrotteling
import kpn.core.util.Log
import kpn.shared.Timestamp
import spray.can.Http
import spray.util.pimpFuture

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt

object RouteLoaderDemo {

  def main(args: Array[String]): Unit = {
    val system = ActorSystemConfig.actorSystem()
    try {
      new RouteLoaderDemo(system).run()
    }
    finally {
      IO(Http)(system).ask(Http.CloseAll)(15.second).await
      Await.result(system.terminate(), Duration.Inf)
    }
  }
}

class RouteLoaderDemo(system: ActorSystem) {

  val log = Log(classOf[RouteLoaderDemo])

  val countryAnalyzer = new CountryAnalyzerImpl()
  val executor = new OverpassQueryExecutorWithThrotteling(system, new OverpassQueryExecutorImpl())
  val routeLoader = new RouteLoaderImpl(executor, countryAnalyzer)

  def run(): Unit = {
    log.unitElapsed {
      routeLoader.loadRoute(Timestamp(2018, 5, 17, 3, 19, 24), 8305522L) match {
        case Some(route) =>
          println(route.name)
        case _ =>
          println("could not load route")
      }
      "routeLoader"
    }
  }
}
