package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.data.raw.RawData
import kpn.api.custom.Relation
import kpn.api.custom.ScopedNetworkType
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerNoop
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute

import scala.xml.InputSource
import scala.xml.XML

class Issue109_RoundaboutRoute extends UnitTest {

  test("analysis") {
    val analysisContext = new AnalysisContext()
    val countryAnalyzer = new CountryAnalyzerNoop()
    val tileCalculator = new TileCalculatorImpl()
    val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val routeCountryAnalyzer = new RouteCountryAnalyzer(countryAnalyzer)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    val relation = readRoute()
    val routeAnalysis = routeAnalyzer.analyze(relation).get

    assert(routeAnalysis.route.facts.isEmpty)
    assert(routeAnalysis.structure.unusedSegments.isEmpty)

    routeAnalysis.route.analysis.map.freeNodes.map(_.id).toSet should equal(
      Set(
        1015045148L,
        302102477L,
        301008714L,
        301008719L,
        301008718L,
        2509722539L,
        2509722616L,
        302941691L,
      )
    )

    routeAnalysis.route.analysis.map.freePaths.map(path => path.startNodeId -> path.endNodeId).toSet should equal(
      Set(
        1015045148L -> 302941691L,
        302941691L -> 1015045148L,
        1015045148L -> 302102477L,
        302102477L -> 1015045148L,
        302941691L -> 2509722616L,
        2509722616L -> 302941691L,
        2509722616L -> 2509722539L,
        2509722539L -> 2509722616L,
        2509722539L -> 301008719L,
        301008719L -> 2509722539L,
        301008719L -> 301008718L,
        301008718L -> 301008719L,
        301008718L -> 301008714L,
        301008714L -> 301008718L,
        301008714L -> 302102477L,
        302102477L -> 301008714L
      )
    )
  }

  private def readRoute(): Relation = {
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

    routeRelation
  }

  private def readData(routeId: Long): RawData = {
    val stream = getClass.getResourceAsStream(s"/case-studies/$routeId.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    new Parser().parse(xml)
  }
}
