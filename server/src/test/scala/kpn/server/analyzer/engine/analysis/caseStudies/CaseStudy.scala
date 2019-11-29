package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Relation
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.NetworkNodeBuilder
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute

import scala.xml.InputSource
import scala.xml.XML

object CaseStudy {

  private val countryAnalyzer = new CountryAnalyzerNoop()

  def routeAnalysis(name: String): RouteAnalysis = {
    val filename = s"/case-studies/$name.xml"
    val (data, networkType, routeRelation) = load(filename)
    val analysisContext = new AnalysisContext(oldTagging = true)
    val networkNodes = new NetworkNodeBuilder(analysisContext, data, networkType, countryAnalyzer).networkNodes
    val tileCalculator = new TileCalculatorImpl()
    val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
    val routeAnalyzer = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl(), routeTileAnalyzer)
    routeAnalyzer.analyze(networkNodes, LoadedRoute(Some(Country.nl), networkType, "", data, routeRelation), orphan = false)
  }

  private def load(filename: String): (Data, NetworkType, Relation) = {

    val stream = getClass.getResourceAsStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)

    val rawData = new Parser().parse(xml)
    if (rawData.relations.isEmpty) {
      throw new IllegalArgumentException(s"No route relation found in file $filename")
    }

    if (rawData.relations.size > 1) {
      throw new IllegalArgumentException(s"Multiple relations found in file $filename (expected 1 single relation only)")
    }

    val rawRouteRelation = rawData.relations.head

    if (!rawRouteRelation.tags.has("type", "route")) {
      throw new IllegalArgumentException(s"Relation does not have expected tag type=route in file $filename")
    }

    val networkType = rawRouteRelation.tags("network") match {
      case Some("rcn") => NetworkType.cycling
      case Some("rwn") => NetworkType.hiking
      case Some("rpn") => NetworkType.canoe
      case _ => throw new IllegalArgumentException("Network type not found in file " + filename)
    }

    val data = new DataBuilder(rawData).data
    val routeRelation = data.relations(rawRouteRelation.id)
    (data, networkType, routeRelation)
  }
}
