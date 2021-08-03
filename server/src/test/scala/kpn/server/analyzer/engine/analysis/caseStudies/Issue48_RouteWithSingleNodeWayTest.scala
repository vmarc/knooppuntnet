package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.api.custom.ScopedNetworkType
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerNoop
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute

import scala.xml.InputSource
import scala.xml.XML

class Issue48_RouteWithSingleNodeWayTest extends UnitTest {

  test("ignore ways with less than 2 nodes in route analysis") {
    val loadedRoute = readRoute()
    val analysisContext = new AnalysisContext()
    val countryAnalyzer = new CountryAnalyzerNoop()
    val tileCalculator = new TileCalculatorImpl()
    val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val routeCountryAnalyzer = new RouteCountryAnalyzer(countryAnalyzer)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    val routeAnalysis = routeAnalyzer.analyze(loadedRoute, orphan = false)
    assert(routeAnalysis.route.facts.contains(Fact.RouteSuspiciousWays))
  }

  private def readRoute(): LoadedRoute = {
    val data = readData()
    val routeRelation = data.relations(2941800L)
    val analysisContext = new AnalysisContext()
    LoadedRoute(ScopedNetworkType.rwn, data, routeRelation)
  }

  private def readData(): Data = {
    val stream = getClass.getResourceAsStream("/case-studies/network-2243640.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    val rawData = new Parser().parse(xml)
    new DataBuilder(rawData).data
  }
}
