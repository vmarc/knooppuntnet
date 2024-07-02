package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteWithoutWays
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerFixed
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.tile.OldLinesTileCalculatorImpl
import kpn.server.analyzer.engine.tile.OldTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

class RouteAnalyzerRouteWithoutWaysTest extends UnitTest with MockFactory {

  test("RouteNotForward and RouteNotBackward should not be reported for routes without ways") {

    val relation = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberNode(2)
    }.data.relations(1L)

    val locationAnalyzer = new LocationAnalyzerFixed()
    val tileCalculator = new OldTileCalculatorImpl()
    val linesTileCalculator = new OldLinesTileCalculatorImpl(tileCalculator)
    val routeTileCalculator = new RouteTileCalculatorImpl(linesTileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val routeRepository = stub[RouteRepository]
    val routeCountryAnalyzer = new RouteCountryAnalyzer(locationAnalyzer, routeRepository)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new RouteDetailMainAnalyzerImpl(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    val context = routeAnalyzer.analyze(relation, None).get
    context.facts.shouldMatchTo(Seq(RouteWithoutWays, RouteBroken))
  }
}
