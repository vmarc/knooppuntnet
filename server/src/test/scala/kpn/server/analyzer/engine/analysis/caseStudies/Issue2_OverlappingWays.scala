package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.LatLonImpl
import kpn.api.common.common.TrackSegment
import kpn.api.common.data.raw.RawData
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.tools.`export`.GeoJsonLineStringGeometry
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerNoop
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.json.Json

import scala.xml.InputSource
import scala.xml.XML

class Issue2_OverlappingWays extends UnitTest {

  test("28-28") {

    fail("reproduces unresolved issue")

    val routeAnalysis = analyze("28-28", "vv", 7776398L, 9174227L)
    println(routeAnalysis.route.analysis.structureStrings)
    routeAnalysis.route.analysis.map.unusedSegments.zipWithIndex.foreach { case (segment, index) =>
      makeGeojson(s"unusedSegment ${index + 1}", segment)
    }
    assert(routeAnalysis.route.facts.isEmpty)
    assert(routeAnalysis.structure.unusedSegments.isEmpty)
  }

  test("32-32") {

    fail("reproduces unresolved issue")

    val routeAnalysis = analyze("32-32", "x", 7175609L, 11047960L)
    println(routeAnalysis.route.analysis.structureStrings)
    routeAnalysis.route.analysis.map.unusedSegments.zipWithIndex.foreach { case (segment, index) =>
      makeGeojson(s"unusedSegment: ${index + 1}", segment)
    }
    assert(routeAnalysis.route.facts.isEmpty)
    assert(routeAnalysis.structure.unusedSegments.isEmpty)
  }

  private def analyze(routeName: String, connectingNodeName: String, routeId1: Long, routeId2: Long): RouteAnalysis = {
    val rawData1 = withoutConnectionNode(readData(routeId1), connectingNodeName)
    val rawData2 = withoutConnectionNode(readData(routeId2), connectingNodeName)
    val rawData = RawData.merge(rawData1, rawData2)
    val data = new DataBuilder(rawData).data

    val routeRelation1 = data.relations(routeId1)
    val routeRelation2 = data.relations(routeId2)

    val routeTags = routeRelation1.tags.without("ref") ++ Tags.from("ref" -> routeName)

    val rawRouteRelation = routeRelation1.raw.copy(
      tags = routeTags,
      members = routeRelation1.raw.members ++ routeRelation2.raw.members.reverse
    )
    val routeRelation = Relation(
      rawRouteRelation,
      routeRelation1.members ++ routeRelation2.members
    )

    val analysisContext = new AnalysisContext()

    val tileCalculator = new TileCalculatorImpl()
    val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val countryAnalyzer = new CountryAnalyzerNoop()
    val routeCountryAnalyzer = new RouteCountryAnalyzer(countryAnalyzer)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    routeAnalyzer.analyze(routeRelation).get
  }

  private def readData(routeId: Long): RawData = {
    val stream = getClass.getResourceAsStream(s"/case-studies/$routeId.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    new Parser().parse(xml)
  }

  private def withoutConnectionNode(rawData: RawData, connectingNodeName: String): RawData = {
    rawData.copy(nodes = rawData.nodes.map(node =>
      if (node.tags.has("rwn_ref", connectingNodeName)) {
        node.copy(tags = node.tags.without("rwn_ref").without("network:type"))
      }
      else {
        node
      }
    ))
  }

  private def makeGeojson(name: String, trackSegment: TrackSegment): Unit = {
    val latlons = trackSegment.trackPoints.map(point => LatLonImpl(point.lat, point.lon))
    val coordinates = latlons.toArray.map(c => Array(c.lon, c.lat))
    val line = GeoJsonLineStringGeometry(
      "LineString",
      coordinates
    )
    val json = Json.objectMapper.writerWithDefaultPrettyPrinter()
    println(s"https://geojson.io/ $name")
    println(json.writeValueAsString(line))
  }
}
