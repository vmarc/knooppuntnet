package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.LatLonImpl
import kpn.api.common.common.TrackSegment
import kpn.api.common.data.raw.RawData
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.GeoJsonLineStringGeometry
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerTest
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tile.RouteTileCache
import kpn.server.json.Json
import kpn.server.repository.RouteRepository
import org.scalamock.stubs.Stubs

import scala.xml.InputSource
import scala.xml.XML

class Issue2_OverlappingWays extends UnitTest with Stubs {

  /*
     Test the analysis of a loop route (end node is the same as the start node), and the ways to get to the
     actual route are included twice.
           _______
     ____/        \vv
         \________/

     To work arround the fact reported in knooppuntnet, the route was split in two routes with a connecting
     node "vv".  In this test we first reconstruct the original route from the two new routes.
   */
  test("28-28") { // reproduces unresolved issue
    val baseRouteAnalysisContext = analyze("28-28", "vv", 7776398L, 9174227L)
    baseRouteAnalysisContext.abort shouldBe false
    baseRouteAnalysisContext.facts shouldBe empty

    val structure = baseRouteAnalysisContext.structure

    val forwardPath = structure.forwardPath.get
    forwardPath.id should equal(1)
    forwardPath.startNodeId should equal(45749578L)
    forwardPath.endNodeId should equal(45749578L)
    forwardPath.id should equal(1)

    val backwardPath = structure.backwardPath.get
    backwardPath.id should equal(2)
    backwardPath.startNodeId should equal(45749578L)
    backwardPath.endNodeId should equal(45749578L)

    structure.startTentaclePaths should equal(Seq.empty)
    structure.endTentaclePaths should equal(Seq.empty)
    structure.otherPaths should equal(Seq.empty)
  }

  test("32-32") {

    val baseRouteAnalysisContext = analyze("32-32", "x", 7175609L, 11047960L)
    baseRouteAnalysisContext.abort shouldBe false
    baseRouteAnalysisContext.facts shouldBe empty

    val structure = baseRouteAnalysisContext.structure

    val forwardPath = structure.forwardPath.get
    forwardPath.id should equal(1)
    forwardPath.startNodeId should equal(908497572L)
    forwardPath.endNodeId should equal(908497572L)
    forwardPath.id should equal(1)

    val backwardPath = structure.backwardPath.get
    backwardPath.id should equal(2)
    backwardPath.startNodeId should equal(908497572L)
    backwardPath.endNodeId should equal(908497572L)

    structure.startTentaclePaths should equal(Seq.empty)
    structure.endTentaclePaths should equal(Seq.empty)
    structure.otherPaths should equal(Seq.empty)
  }

  private def analyze(routeName: String, connectingNodeName: String, routeId1: Long, routeId2: Long): BaseRouteAnalysisContext = {
    val rawData1 = withoutConnectionNode(readData(routeId1), connectingNodeName)
    val rawData2 = withoutConnectionNode(readData(routeId2), connectingNodeName)
    val rawData = RawData.merge(rawData1, rawData2)
    val data = new DataBuilder(rawData).data

    val routeRelation1 = data.relations(routeId1)
    val routeRelation2 = data.relations(routeId2)

    val routeTags = routeRelation1.tags.filterNot(_.key == "ref") ++ Tags.from("ref" -> routeName)

    val routeRelation = routeRelation1.copy(
      tags = routeTags,
      members = routeRelation1.members ++ routeRelation2.members.reverse
    )

    val routeTileCache = new RouteTileCache()
    val lineSegmentTileCalculator = new LineSegmentTileCalculator(routeTileCache)
    val routeTileAnalyzer = new BaseRouteTileAnalyzer(lineSegmentTileCalculator)
    val locationAnalyzer = LocationAnalyzerTest.locationAnalyzer
    val routeRepository = stub[RouteRepository]
    val routeCountryAnalyzer = new BaseRouteCountryAnalyzerImpl(locationAnalyzer, routeRepository)
    val routeLocationAnalyzer = new BaseRouteLocationAnalyzerMock()
    val routeAnalyzer = new BaseRouteMainAnalyzer(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    routeAnalyzer.analyze(routeRelation, None)
  }

  private def readData(routeId: Long): RawData = {
    val stream = getClass.getResourceAsStream(s"/case-studies/$routeId.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    new Parser(includeMetadata = false).parse(xml)
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
    println(s"https://geojson.io/ $name")
    println(Json.pretty(line))
  }
}
