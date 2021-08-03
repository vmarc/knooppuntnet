package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeInfoAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.RouteLoaderImpl

object RouteAnalyzerDemo {
  def main(args: Array[String]): Unit = {
    val analysisContext = new AnalysisContext()
    val executor = new OverpassQueryExecutorImpl()
    val routeLoader = new RouteLoaderImpl(executor)

    routeLoader.loadRoute(Timestamp(2018, 5, 24, 8, 59, 2), 101673) match {
      case Some(loadedRoute) =>
        val countryAnalyzer = new CountryAnalyzerNoop()
        val tileCalculator = new TileCalculatorImpl()
        val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
        val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
        val routeCountryAnalyzer = new RouteCountryAnalyzer(countryAnalyzer)
        val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
        val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
        val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(analysisContext, oldNodeAnalyzer)
        val routeAnalyzer = new MasterRouteAnalyzerImpl(
          analysisContext,
          routeCountryAnalyzer,
          routeLocationAnalyzer,
          routeTileAnalyzer,
          routeNodeInfoAnalyzer
        )
        val routeAnalysis = routeAnalyzer.analyze(loadedRoute, orphan = true)
        println("facts=" + routeAnalysis.route.facts)
      case None => println("could not load route")
    }
  }
}
