package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.ScopedNetworkType
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeInfoAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute

import scala.xml.InputSource
import scala.xml.XML

class Issue48_RouteWithSingleNodeWayTest extends UnitTest {

  test("ignore ways with less than 2 nodes in route analysis") {
    val loadedRoute = readRoute()
    val analysisContext = new AnalysisContext(oldTagging = true)
    val tileCalculator = new TileCalculatorImpl()
    val routeTileAnalyzer = new RouteTileCalculatorImpl(tileCalculator)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(analysisContext, nodeAnalyzer)
    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeLocationAnalyzer,
      routeTileAnalyzer,
      routeNodeInfoAnalyzer
    )
    val routeAnalysis = routeAnalyzer.analyze(loadedRoute, orphan = false)
    assert(routeAnalysis.route.facts.contains(Fact.RouteSuspiciousWays))
  }

  private def readRoute(): LoadedRoute = {
    val data = readData()
    val routeRelation = data.relations(2941800L)
    val analysisContext = new AnalysisContext(oldTagging = true)
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    LoadedRoute(Some(Country.nl), ScopedNetworkType.rwn, data, routeRelation)
  }

  private def readData(): Data = {
    val stream = getClass.getResourceAsStream("/case-studies/network-2243640.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    val rawData = new Parser().parse(xml)
    new DataBuilder(rawData).data
  }
}
