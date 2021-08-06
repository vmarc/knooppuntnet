package kpn.core.history

import kpn.api.custom.Timestamp
import kpn.core.overpass.CachingOverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.LocationConfiguration
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.analysis.location.OldNodeLocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.OldMainNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.NetworkLoaderImpl

import java.io.File

object NetworkDiffAnalyzerDemo {

  def main(args: Array[String]): Unit = {

    val networkId = 1341164L
    val routeId = 2655355L
    val timestampBefore = Timestamp(2017, 11, 9, 10, 34, 58)
    val timestampAfter = Timestamp(2017, 8, 11, 10, 35, 59)

    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
    val tileCalculator = new TileCalculatorImpl()
    val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val routeCountryAnalyzer: RouteCountryAnalyzer = null
    val routeLocationAnalyzer: RouteLocationAnalyzer = null
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()

    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )

    val locationConfiguration: LocationConfiguration = new LocationConfigurationReader().read()

    val oldNodeLocationAnalyzer = new OldNodeLocationAnalyzerImpl(
      locationConfiguration,
      analyzerEnabled = true
    )

    val oldMainNodeAnalyzer = new OldMainNodeAnalyzerImpl(
      countryAnalyzer,
      oldNodeLocationAnalyzer
    )

    val networkNodeAnalyzer = new NetworkNodeAnalyzerImpl(
      oldMainNodeAnalyzer,
      oldNodeAnalyzer
    )

    val networkRouteAnalyzer = new NetworkRouteAnalyzerImpl(
      countryAnalyzer,
      routeAnalyzer
    )

    val networkAnalyzer = new NetworkAnalyzerImpl(
      relationAnalyzer,
      networkNodeAnalyzer,
      networkRouteAnalyzer
    )

    val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(
      relationAnalyzer,
      countryAnalyzer
    )

    val networkLoader = {
      val executor = new OverpassQueryExecutorImpl()
      val cachingExecutor = new CachingOverpassQueryExecutor(new File("/kpn/cache"), executor)
      new NetworkLoaderImpl(cachingExecutor)
    }

    //    val snapshotBefore = {
    //      val loadedNetwork = networkLoader.load(Some(timestampBefore), networkId).get
    //      val t1 = System.currentTimeMillis()
    //      val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetwork.relation)
    //      val network = networkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork.data, networkId)
    //      val t2 = System.currentTimeMillis()
    //      println(s"after analysis in ${t2 - t1}ms")
    //      NetworkSnapshot(timestampBefore, loadedNetwork.data, network)
    //    }

    val snapshotAfter = {
      val loadedNetwork = networkLoader.load(Some(timestampAfter), networkId).get
      val t1 = System.currentTimeMillis()
      val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetwork.relation)
      val network = networkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork)
      val t2 = System.currentTimeMillis()
      println(s"after analysis in ${t2 - t1}ms")
      NetworkSnapshot(timestampAfter, loadedNetwork.data, network)
    }

    val route = snapshotAfter.network.routes.find(_.id == routeId).get

    println(route.routeAnalysis.route.facts)



    //    val networkDiff = new NetworkDiffAnalyzer(snapshotBefore, snapshotAfter).diff
    //    println("updated routes = " + networkDiff.routes.updated.size)
    //
    //    val route = snapshotBefore.network.routes.find(_.id == routeId)
    //
    //    println("route = " + route)
  }
}
