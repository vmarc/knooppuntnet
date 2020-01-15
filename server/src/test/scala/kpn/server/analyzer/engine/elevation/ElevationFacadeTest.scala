package kpn.server.analyzer.engine.elevation

import kpn.api.common.LatLon
import kpn.api.common.LatLonImpl
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy
import org.locationtech.jts.densify.Densifier
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ElevationFacadeTest extends FunSuite with Matchers {

  test("route elevation profile") {

    val km = Haversine.km(47, 14, 48, 14)
    println(s"km ${km}")

    // load route 10460202 50-53 Austria
    // See: https://cycling.waymarkedtrails.org/#route?id=10460202&map=15!47.2252!14.777
    // length 6.01 km
    // expected accumulated ascent: 85 m
    // expected accumulated descent: 100 m

    val path = CaseStudy.routeAnalysis("10460202").route.analysis.get.map.forwardPath.get

    val trackPoints = path.segments.flatMap(_.fragments.map(_.trackPoint))
    val latLons = trackPoints.map(p => LatLonImpl(p.lat, p.lon))

    println(s"coordinate count ${latLons.size}")

    val coords = new Array[Coordinate](latLons.size)
    latLons.zipWithIndex.foreach { case (latLon, index) =>
      coords(index) = new Coordinate(latLon.lat, latLon.lon)
    }

    val lineString = new GeometryFactory().createLineString(coords)

    val g: Geometry = Densifier.densify(lineString, 50L / (km * 1000))

    println(s"coordinate count densified ${g.getCoordinates.length}")

    val repo = new ElevationRepositoryImpl()
    val densified: Seq[LatLon] = g.getCoordinates.toSeq.map(c => LatLonImpl.from(c.getX, c.getY))

    val elevations = densified.flatMap { latLon =>
      repo.elevation(latLon)
    }

    val ascending = elevations.sliding(2).map { case Seq(a, b) => b - a }.filter(_ > 0).sum
    val descending = 0 - elevations.sliding(2).map { case Seq(a, b) => b - a }.filter(_ < 0).sum

    println(s"ascending=$ascending")
    println(s"descending=$descending")

    println(elevations)
    //elevations.foreach(println)

    val distances1 = latLons.sliding(2).map { case Seq(a, b) => (Haversine.km(a.lat, a.lon, b.lat, b.lon) * 1000).toInt }
    val distances2 = densified.sliding(2).map { case Seq(a, b) => (Haversine.km(a.lat, a.lon, b.lat, b.lon) * 1000).toInt }

    println(distances1.mkString(", "))
    println(distances2.mkString(", "))
  }

}
