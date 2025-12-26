package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Relation
import kpn.api.common.route.RouteNodes
import kpn.core.data.Data
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteCountryAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.structure.RouteDetailAnalysisTestContext
import kpn.server.analyzer.engine.analysis.route.structure.StructureElementAnalyzer
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tile.RouteTileCache

class StructureTestSetup(val data: Data) {

  def elementGroups(traceEnabled: Boolean = false): Seq[Seq[String]] = {
    val elementGroups = StructureElementAnalyzer.analyze(RouteNodes(), relation.members, traceEnabled)
    if (traceEnabled) {
      println("\nResult:")
      elementGroups.zipWithIndex.foreach { case (elementGroup, groupIndex) =>
        elementGroup.elements.zipWithIndex.foreach { case (element, elementIndex) =>
          println(s"  group=${groupIndex + 1}, element=${elementIndex + 1}: ${element.string}")
        }
      }
    }
    elementGroups.map(_.elements.map(_.string))
  }

  def analyze(traceEnabled: Boolean = false): RouteDetailAnalysisTestContext = {
    val routeTileCache = new RouteTileCache()
    val lineSegmentTileCalculator = new LineSegmentTileCalculator(routeTileCache)
    val routeTileAnalyzer = new BaseRouteTileAnalyzer(lineSegmentTileCalculator)
    val routeCountryAnalyzer = new BaseRouteCountryAnalyzerMock()
    val routeLocationAnalyzer = new BaseRouteLocationAnalyzerMock()
    val routeAnalyzer = new BaseRouteMainAnalyzer(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    RouteDetailAnalysisTestContext(
      routeAnalyzer.analyze(relation, None, traceEnabled)
    )
  }

  private def relation: Relation = {
    data.relations(1)
  }
}
