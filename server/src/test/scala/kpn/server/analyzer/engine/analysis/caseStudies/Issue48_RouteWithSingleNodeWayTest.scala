package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute

import scala.xml.InputSource
import scala.xml.XML

class Issue48_RouteWithSingleNodeWayTest extends UnitTest {

  test("ingore ways with less than 2 nodes in route analysis") {
    val loadedRoute = readRoute()
    val analysisContext = new AnalysisContext(oldTagging = true)
    val tileCalculator = new TileCalculatorImpl()
    val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeLocationAnalyzer,
      new AccessibilityAnalyzerImpl(),
      routeTileAnalyzer
    )
    val routeAnalysis = routeAnalyzer.analyze(Map(), loadedRoute, orphan = false)
    routeAnalysis.route.facts.contains(Fact.RouteSuspiciousWays) should equal(true)
  }

  private def readRoute(): LoadedRoute = {
    val data = readData()
    val routeRelation = data.relations(2941800L)
    val analysisContext = new AnalysisContext(oldTagging = true)
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val name = relationAnalyzer.routeName(routeRelation)
    LoadedRoute(Some(Country.nl), NetworkType.hiking, name, data, routeRelation)
  }

  private def readData(): Data = {
    val stream = getClass.getResourceAsStream("/case-studies/network-2243640.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    val rawData = new Parser().parse(xml)
    new DataBuilder(rawData).data
  }
}
