package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.data.raw.RawData
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Relation
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute

import scala.xml.InputSource
import scala.xml.XML

class Issue109_SplitNodeRoute extends UnitTest {

  test("analysis") {
    val loadedRoute = readRoute()
    val analysisContext = new AnalysisContext()
    val tileCalculator = new TileCalculatorImpl()
    val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    val routeAnalysis = routeAnalyzer.analyze(loadedRoute, orphan = false)

    routeAnalysis.route.facts.isEmpty should equal(true)
    routeAnalysis.structure.unusedSegments.isEmpty should equal(true)

    val paths = routeAnalysis.structure.splitNodePaths

    val pathNodeNames = paths.map { path =>
      val start = path.start.map(_.alternateName).getOrElse("")
      val end = path.end.map(_.alternateName).getOrElse("")
      start -> end
    }.toSet

    pathNodeNames should equal(
      Set(
        "45.a" -> "45.b",
        "45.b" -> "45.c",
        "45.c" -> "45.d",
        "45.d" -> "45.e",
        "45.e" -> "45.f",
        "45.f" -> "45.g",
        "45.g" -> "45",
        "45" -> "45.a"
      )
    )
  }

  private def readRoute(): LoadedRoute = {
    val rawData1 = readData(11512870L)
    val rawData2 = readData(11512871L)
    val rawData = RawData.merge(rawData1, rawData2)
    val data = new DataBuilder(rawData).data

    val routeRelation1 = data.relations(11512870L)
    val routeRelation2 = data.relations(11512871L)

    val rawRouteRelation = routeRelation1.raw.copy(members = routeRelation1.raw.members ++ routeRelation2.raw.members)
    val routeRelation = Relation(
      rawRouteRelation,
      routeRelation1.members ++ routeRelation2.members
    )

    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val name = relationAnalyzer.routeName(routeRelation)
    LoadedRoute(Some(Country.nl), NetworkType.cycling, name, data, routeRelation)
  }

  private def readData(routeId: Long): RawData = {
    val stream = getClass.getResourceAsStream(s"/case-studies/${routeId}.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    new Parser().parse(xml)
  }
}
