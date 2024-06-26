package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Relation
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerFixed
import kpn.server.analyzer.engine.analysis.route.MainRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.tile.OldLinesTileCalculatorImpl
import kpn.server.analyzer.engine.tile.OldTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

import scala.xml.InputSource
import scala.xml.XML

object CaseStudy extends MockFactory {

  def routeAnalysis(name: String): RouteAnalysis = {
    val filename = s"/case-studies/$name.xml"
    val routeRelation = load(filename)
    val locationAnalyzer = new LocationAnalyzerFixed()
    val tileCalculator = new OldTileCalculatorImpl()
    val linesTileCalculator = new OldLinesTileCalculatorImpl(tileCalculator)
    val routeTileCalculator = new RouteTileCalculatorImpl(linesTileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val routeRepository = stub[RouteRepository]
    val routeCountryAnalyzer = new RouteCountryAnalyzer(locationAnalyzer, routeRepository)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new MainRouteAnalyzerImpl(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    routeAnalyzer.analyze(routeRelation, None /* TODO redesign - hierarchy */).get
  }

  def load(filename: String): Relation = {

    val stream = getClass.getResourceAsStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)

    val rawData = new Parser(full = false).parse(xml)
    if (rawData.relations.isEmpty) {
      throw new IllegalArgumentException(s"No route relation found in file $filename")
    }

    if (rawData.relations.size > 1) {
      throw new IllegalArgumentException(s"Multiple relations found in file $filename (expected 1 single relation only)")
    }

    val rawRouteRelation = rawData.relations.head

    if (!rawRouteRelation.hasTag("type", "route")) {
      throw new IllegalArgumentException(s"Relation does not have expected tag type=route in file $filename")
    }

    new DataBuilder(rawData).data.relations(rawRouteRelation.id)
  }
}
