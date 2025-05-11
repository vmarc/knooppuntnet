package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Relation
import kpn.core.data.DataBuilder
import kpn.core.doc.BaseRouteDoc
import kpn.core.loadOld.Parser
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerFixed
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

import scala.xml.InputSource
import scala.xml.XML

object CaseStudy extends MockFactory {

  def analyze(name: String): BaseRouteAnalysisContext = {
    val filename = s"/case-studies/$name.xml"
    val routeRelation = load(filename)
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
    routeAnalyzer.analyze(routeRelation, None /* TODO redesign - hierarchy */)
  }

  def baseRouteDoc(name: String): BaseRouteDoc = {
    new BaseRouteDocBuilder().build(analyze(name))
  }

  def load(filename: String): Relation = {

    val stream = getClass.getResourceAsStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)

    val rawData = new Parser(full = false).parse(xml)
    if (rawData.relations.isEmpty) {
      throw new IllegalArgumentException(s"No route relation found in file $filename")
    }

    if (rawData.relations.sizeIs > 1) {
      throw new IllegalArgumentException(s"Multiple relations found in file $filename (expected 1 single relation only)")
    }

    val rawRouteRelation = rawData.relations.head

    if (!rawRouteRelation.hasTag("type", "route")) {
      throw new IllegalArgumentException(s"Relation does not have expected tag type=route in file $filename")
    }

    new DataBuilder(rawData).data.relations(rawRouteRelation.id)
  }
}
