package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Relation
import kpn.core.data.Data
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerTest
import kpn.server.analyzer.engine.analysis.route.RouteDetailAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteDetailMainAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.tile.OldLinesTileCalculatorImpl
import kpn.server.analyzer.engine.tile.OldTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

class StructureTestSetup(val data: Data) extends MockFactory {

  def elementGroups(traceEnabled: Boolean = false): Seq[Seq[String]] = {
    val elementGroups = StructureElementAnalyzer.analyze(relation.members, traceEnabled)
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

  def reference(traceEnabled: Boolean = false): Seq[String] = {
    val referenceStructure = new RouteLinkAnalyzer(traceEnabled).analyze(relation)
    val strings = referenceStructure.links.zipWithIndex.map { case (wayInfo, index) => s"${index + 1}    ${wayInfo.reportString}" }
    if (traceEnabled) println()
    if (traceEnabled) strings.foreach(println)
    if (traceEnabled) println()
    strings
  }

  def structure(traceEnabled: Boolean = false): TestStructure = {
    val elementGroups = StructureElementAnalyzer.analyze(relation.members, traceEnabled)
    TestStructure.from(new StructureAnalyzer(traceEnabled).analyze(RouteNodeAnalysis(), elementGroups))
  }

  def analyze(): RouteDetailAnalysis = {
    val tileCalculator = new OldTileCalculatorImpl()
    val linesTileCalculator = new OldLinesTileCalculatorImpl(tileCalculator)
    val routeTileCalculator = new RouteTileCalculatorImpl(linesTileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val locationAnalyzer = LocationAnalyzerTest.locationAnalyzer
    val routeRepository = stub[RouteRepository]
    val routeCountryAnalyzer = new RouteCountryAnalyzer(locationAnalyzer, routeRepository)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new RouteDetailMainAnalyzerImpl(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    routeAnalyzer.analyze(relation, None).get
  }

  private def relation: Relation = {
    data.relations(1)
  }
}
