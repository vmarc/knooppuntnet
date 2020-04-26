package kpn.core.history

import java.io.File

import kpn.api.custom.Timestamp
import kpn.core.overpass.CachingOverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.NetworkLoaderImpl

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
    val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
    val routeLocationAnalyzer: RouteLocationAnalyzer = null

    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeLocationAnalyzer,
      new AccessibilityAnalyzerImpl(),
      routeTileAnalyzer
    )
    val networkAnalyzer = new NetworkAnalyzerImpl(analysisContext, relationAnalyzer, countryAnalyzer, routeAnalyzer)
    val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer)

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
