package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.RouteLoaderImpl

object RouteAnalyzerDemo {
  def main(args: Array[String]): Unit = {
    val analysisContext = new AnalysisContext()
    val countryAnalyzer = {
      val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
      new CountryAnalyzerImpl(relationAnalyzer)
    }
    val executor = new OverpassQueryExecutorImpl()
    val routeLoader = new RouteLoaderImpl(executor, countryAnalyzer)

    routeLoader.loadRoute(Timestamp(2018, 5, 24, 8, 59, 2), 101673) match {
      case Some(loadedRoute) =>
        val routeAnalysis = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl()).analyze(Map(), loadedRoute, orphan = true)
        println("facts=" + routeAnalysis.route.facts)
      case None => println("could not load route")
    }
  }
}
