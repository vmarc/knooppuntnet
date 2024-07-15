package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.LatLonImpl
import kpn.api.common.common.TrackSegment
import kpn.api.common.data.raw.RawData
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.GeoJsonLineStringGeometry
import kpn.core.util.Redesign
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerTest
import kpn.server.analyzer.engine.analysis.route.RouteDetailAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteDetailMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.OldRouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculatorImpl
import kpn.server.analyzer.engine.tile.OldLinesTileCalculatorImpl
import kpn.server.analyzer.engine.tile.OldTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.json.Json
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory

import scala.xml.InputSource
import scala.xml.XML

class Issue2_OverlappingWays extends UnitTest with MockFactory {

  test("28-28") {
    if (Redesign.enablePendingTests) {

      fail("reproduces unresolved issue")

      val routeAnalysis = analyze("28-28", "vv", 7776398L, 9174227L)
      println(routeAnalysis.routeDetail.analysis.structureStrings)
      routeAnalysis.routeDetail.analysis.map.unusedSegments.zipWithIndex.foreach { case (segment, index) =>
        makeGeojson(s"unusedSegment ${index + 1}", segment)
      }
      assert(routeAnalysis.routeDetail.facts.isEmpty)
      assert(routeAnalysis.structure.unusedSegments.isEmpty)
    }
  }

  test("32-32") {

    if (Redesign.enablePendingTests) {
      fail("reproduces unresolved issue")

      val routeAnalysis = analyze("32-32", "x", 7175609L, 11047960L)
      println(routeAnalysis.routeDetail.analysis.structureStrings)
      routeAnalysis.routeDetail.analysis.map.unusedSegments.zipWithIndex.foreach { case (segment, index) =>
        makeGeojson(s"unusedSegment: ${index + 1}", segment)
      }
      assert(routeAnalysis.routeDetail.facts.isEmpty)
      assert(routeAnalysis.structure.unusedSegments.isEmpty)
    }
  }

  private def analyze(routeName: String, connectingNodeName: String, routeId1: Long, routeId2: Long): RouteDetailAnalysis = {
    val rawData1 = withoutConnectionNode(readData(routeId1), connectingNodeName)
    val rawData2 = withoutConnectionNode(readData(routeId2), connectingNodeName)
    val rawData = RawData.merge(rawData1, rawData2)
    val data = new DataBuilder(rawData).data

    val routeRelation1 = data.relations(routeId1)
    val routeRelation2 = data.relations(routeId2)

    val routeTags = routeRelation1.tags.filterNot(_.key == "ref") ++ Tags.from("ref" -> routeName)

    val routeRelation = routeRelation1.copy(
      tags = routeTags,
      members = routeRelation1.members ++ routeRelation2.members
    )

    val oldTileCalculator = new OldTileCalculatorImpl()
    val tileCalculator = new TileCalculatorImpl()
    val linesTileCalculator = new OldLinesTileCalculatorImpl(oldTileCalculator)
    val lineSegmentTileCalculator = new LineSegmentTileCalculatorImpl(tileCalculator)
    val routeTileCalculator = new RouteTileCalculatorImpl(lineSegmentTileCalculator)
    val oldRouteTileAnalyzer = new OldRouteTileAnalyzer(routeTileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(lineSegmentTileCalculator)
    val locationAnalyzer = LocationAnalyzerTest.locationAnalyzer
    val routeRepository = stub[RouteRepository]
    val routeCountryAnalyzer = new RouteCountryAnalyzerImpl(locationAnalyzer, routeRepository)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new RouteDetailMainAnalyzer(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      oldRouteTileAnalyzer,
      routeTileAnalyzer
    )
    routeAnalyzer.analyze(routeRelation, None).get.oldRouteDetailAnalysis
  }

  private def readData(routeId: Long): RawData = {
    val stream = getClass.getResourceAsStream(s"/case-studies/$routeId.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    new Parser(full = false).parse(xml)
  }

  private def withoutConnectionNode(rawData: RawData, connectingNodeName: String): RawData = {
    rawData.copy(
      nodes = rawData.nodes.map(node =>
        if (node.hasTag("rwn_ref", connectingNodeName)) {
          node.copy(tags = node.tags.filterNot(_.key == "rwn_ref").filterNot(_.key == "network:type"))
        }
        else {
          node
        }
      )
    )
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
