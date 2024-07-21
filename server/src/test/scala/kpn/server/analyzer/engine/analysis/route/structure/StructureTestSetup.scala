package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.route.RouteNodes
import kpn.api.custom.Relation
import kpn.core.data.Data
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteDetailMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.OldLinesTileCalculatorImpl
import kpn.server.analyzer.engine.tile.OldTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

class StructureTestSetup(val data: Data) extends MockFactory {

  def elementGroups(traceEnabled: Boolean = false): Seq[Seq[String]] = {
    val elementGroups = StructureElementAnalyzer.analyze(RouteNodes(), relation.members, traceEnabled)
    if (traceEnabled) {
      println("\nResult:")
      elementGroups.zipWithIndex.map { case (elementGroup, groupIndex) =>
        elementGroup.elements.zipWithIndex.foreach { case (element, elementIndex) =>
          println(s"  group=${groupIndex + 1}, element=${elementIndex + 1}: ${element.string}")
        }
      }
    }
    elementGroups.map(_.elements.map(_.string))
  }

  //  def structure(traceEnabled: Boolean = false): TestStructure = {
  //    val elementGroups = StructureElementAnalyzer.analyze(RouteNodes(), relation.members, traceEnabled)
  //    TestStructure.from(new OldStructureAnalyzer(traceEnabled).analyze(RouteNodes(), elementGroups))
  //  }

  def analyze(traceEnabled: Boolean = false): RouteDetailAnalysisTestContext = {
    val oldTileCalculator = new OldTileCalculatorImpl()
    val tileCalculator = new TileCalculatorImpl()
    val linesTileCalculator = new OldLinesTileCalculatorImpl(oldTileCalculator)
    val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(tileCalculator)
    val routeTileCalculator = new RouteTileCalculatorImpl(lineSegmentTileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(lineSegmentTileCalculator)
    val locationAnalyzer = stub[LocationAnalyzer]
    val routeRepository = stub[RouteRepository]
    val routeCountryAnalyzer = new RouteCountryAnalyzerMock()
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new RouteDetailMainAnalyzer(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    RouteDetailAnalysisTestContext(
      routeAnalyzer.analyze(relation, None, traceEnabled).get
    )
  }

  private def relation: Relation = {
    data.relations(1)
  }
}
