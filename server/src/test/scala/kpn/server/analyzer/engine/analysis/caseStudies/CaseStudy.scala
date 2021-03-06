package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Country
import kpn.api.custom.Relation
import kpn.api.custom.ScopedNetworkType
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeInfoAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute

import scala.xml.InputSource
import scala.xml.XML

object CaseStudy {

  def routeAnalysis(name: String, oldTagging: Boolean = false): RouteAnalysis = {
    val filename = s"/case-studies/$name.xml"
    val (data, scopedNetworkType, routeRelation) = load(filename)
    val analysisContext = new AnalysisContext(oldTagging = oldTagging)
    val tileCalculator = new TileCalculatorImpl()
    val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(analysisContext, nodeAnalyzer)
    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeLocationAnalyzer,
      routeTileAnalyzer,
      routeNodeInfoAnalyzer
    )
    val loadedRoute = LoadedRoute(Some(Country.nl), scopedNetworkType, data, routeRelation)
    routeAnalyzer.analyze(loadedRoute, orphan = false)
  }

  private def load(filename: String): (Data, ScopedNetworkType, Relation) = {

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

    val scopedNetworkType = RelationAnalyzer.scopedNetworkType(rawRouteRelation).get

    val data = new DataBuilder(rawData).data
    val routeRelation = data.relations(rawRouteRelation.id)
    (data, scopedNetworkType, routeRelation)
  }
}
