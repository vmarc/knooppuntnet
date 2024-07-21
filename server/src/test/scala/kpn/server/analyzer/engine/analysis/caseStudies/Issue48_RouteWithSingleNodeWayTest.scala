package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.api.custom.Relation
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerFixed
import kpn.server.analyzer.engine.analysis.route.RouteDetailMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.OldLinesTileCalculatorImpl
import kpn.server.analyzer.engine.tile.OldTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

import scala.xml.InputSource
import scala.xml.XML

class Issue48_RouteWithSingleNodeWayTest extends UnitTest with MockFactory {

  test("ignore ways with less than 2 nodes in route analysis") {
    val routeRelation = readRoute()
    val locationAnalyzer = new LocationAnalyzerFixed()
    val oldTileCalculator = new OldTileCalculatorImpl()
    val tileCalculator = new TileCalculatorImpl()
    val linesTileCalculator = new OldLinesTileCalculatorImpl(oldTileCalculator)
    val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(tileCalculator)
    val routeTileCalculator = new RouteTileCalculatorImpl(lineSegmentTileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(lineSegmentTileCalculator)
    val routeRepository = stub[RouteRepository]
    val routeCountryAnalyzer = new RouteCountryAnalyzerImpl(locationAnalyzer, routeRepository)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new RouteDetailMainAnalyzer(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    val context = routeAnalyzer.analyze(routeRelation, None).get
    assert(context.facts.contains(Fact.RouteSuspiciousWays))
  }

  private def readRoute(): Relation = {
    val data = readData()
    data.relations(2941800L)
  }

  private def readData(): Data = {
    val stream = getClass.getResourceAsStream("/case-studies/network-2243640.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    val rawData = new Parser(full = false).parse(xml)
    new DataBuilder(rawData).data
  }
}
