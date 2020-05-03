package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
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
        val tileCalculator = new TileCalculatorImpl()
        val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
        val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
        val routeAnalyzer = new MasterRouteAnalyzerImpl(
          analysisContext,
          routeLocationAnalyzer,
          routeTileAnalyzer
        )
        val routeAnalysis = routeAnalyzer.analyze(loadedRoute, orphan = true)
        println("facts=" + routeAnalysis.route.facts)
      case None => println("could not load route")
    }
  }
}
