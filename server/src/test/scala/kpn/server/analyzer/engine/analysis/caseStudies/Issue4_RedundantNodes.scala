package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.LatLonImpl
import kpn.api.common.common.TrackSegment
import kpn.api.custom.Fact
import kpn.core.util.GeoJsonLineStringGeometry
import kpn.core.util.UnitTest
import kpn.server.json.Json

class Issue4_RedundantNodes extends UnitTest {

  test("route 21-70") {
    val route = CaseStudy.routeAnalysis("3792006").route
    route.facts should equal(Seq.empty)
  }

  test("route 24-32") {

    fail("reproduces unresolved issue")

    val route = CaseStudy.routeAnalysis("3330377").route

    println(route.analysis.structureStrings)
    route.analysis.map.unusedSegments.zipWithIndex.foreach { case (segment, index) =>
      makeGeojson(s"unusedSegment ${index + 1}", segment)
    }

    route.analysis.map.startNodes.map(_.name).sorted should equal(Seq("24"))
    route.analysis.map.endNodes.map(_.name).sorted should equal(Seq("32"))
    route.analysis.map.redundantNodes.map(_.name).sorted should equal(
      Seq(
        "83",
        "84",
        "85",
        "86"
      )
    )

    route.facts should equal(Seq(Fact.RouteRedundantNodes))
  }

  test("route 56-58") {

    fail("reproduces unresolved issue")

    val route = CaseStudy.routeAnalysis("3715798").route
    println(route.analysis.structureStrings)
    route.analysis.map.unusedSegments.zipWithIndex.foreach { case (segment, index) =>
      makeGeojson(s"unusedSegment ${index + 1}", segment)
    }
    route.analysis.map.startNodes.map(_.name).sorted should equal(Seq("56"))
    route.analysis.map.endNodes.map(_.name).sorted should equal(Seq("58"))
    route.analysis.map.redundantNodes.map(_.name).sorted should equal(
      Seq(
        "03",
        "04",
        "07",
        "08",
        "24"
      )
    )

    route.facts should equal(Seq(Fact.RouteRedundantNodes))
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
