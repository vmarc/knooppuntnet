package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.Fact.RouteBroken
import kpn.api.common.Fact.RouteWithoutWays
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerFixed
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

class RouteAnalyzerRouteDetailWithoutWaysTest extends UnitTest with MockFactory {

  test("RouteNotForward and RouteNotBackward should not be reported for routes without ways") {

    val relation = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberNode(2)
    }.data.relations(1L)

    val locationAnalyzer = new LocationAnalyzerFixed()
    val tileCalculator = new TileCalculatorImpl()
    val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(tileCalculator)
    val routeTileAnalyzer = new BaseRouteTileAnalyzer(lineSegmentTileCalculator)
    val routeRepository = stub[RouteRepository]
    val routeCountryAnalyzer = new BaseRouteCountryAnalyzerImpl(locationAnalyzer, routeRepository)
    val routeLocationAnalyzer = new BaseRouteLocationAnalyzerMock()
    val routeAnalyzer = new BaseRouteMainAnalyzer(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    val context = routeAnalyzer.analyze(relation, None)
    assertEqual(
      context.facts.toSet,
      Set(RouteWithoutWays, RouteBroken)
    )
  }
}
