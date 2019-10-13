package kpn.server.analyzer.engine.analysis.route

import akka.io.IO
import akka.pattern.ask
import kpn.core.app.ActorSystemConfig
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.server.analyzer.load.RouteLoaderImpl
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorWithThrotteling
import kpn.server.analyzer.engine.AnalysisContext
import kpn.shared.Timestamp
import spray.can.Http
import spray.util.pimpFuture

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt

object RouteAnalyzerDemo {
  def main(args: Array[String]): Unit = {
    val system = ActorSystemConfig.actorSystem()
    try {
      val analysisContext = new AnalysisContext()
      val countryAnalyzer = {
        val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
        new CountryAnalyzerImpl(relationAnalyzer)
      }
      val executor = new OverpassQueryExecutorWithThrotteling(system, new OverpassQueryExecutorImpl())
      val routeLoader = new RouteLoaderImpl(executor, countryAnalyzer)

      routeLoader.loadRoute(Timestamp(2018, 5, 24, 8, 59, 2), 101673) match {
        case Some(loadedRoute) =>
          val routeAnalysis = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl()).analyze(Map(), loadedRoute, orphan = true)
          println("facts=" + routeAnalysis.route.facts)
        case None => println("could not load route")
      }
    }
    finally {
      IO(Http)(system).ask(Http.CloseAll)(15.second).await
      Await.result(system.terminate(), Duration.Inf)
    }
  }
}
