package kpn.server.analyzer.engine.elevation

import kpn.api.common.common.TrackPoint
import kpn.core.common.LatLonD
import kpn.core.gpx.GpxFile
import kpn.core.gpx.GpxSegment
import kpn.core.gpx.GpxWriter
import kpn.core.gpx.WayPoint
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy
import org.locationtech.jts.densify.Densifier
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.annotation.tailrec

class ElevationFacadeTest extends FunSuite with Matchers {

  test("route elevation profile") {

    // load route 10460202 50-53 Austria
    // See: https://cycling.waymarkedtrails.org/#route?id=10460202&map=15!47.2252!14.777
    // length 6.01 km
    // expected accumulated ascent: 85 m
    // expected accumulated descent: 100 m

    val forwardPath = loadForwardPath("10460202")
    val densified = densify(forwardPath)
    val densified2 = altDensify(forwardPath)

    printInfo("original", forwardPath)
    printInfo("densified", densified)
    printInfo("densified2", densified2)

    val repo = new ElevationRepositoryImpl()

    // calculate elevations first time to warm up the tile cache
    densified2.flatMap(repo.elevation)

    val t1 = System.currentTimeMillis()
    val elevations = densified2.flatMap(repo.elevation)
    val t2 = System.currentTimeMillis()
    println(s"elevations calculated in ${t2 - t1}ms")

    val ascending = deltas(elevations).filter(_ > 0).sum
    val descending = 0 - deltas(elevations).filter(_ < 0).sum
    println(s"ascending: ${ascending}m")
    println(s"descending: ${descending}m")

    println(elevations)
    // elevations.reverse.foreach(println)

    println(gpx(densified2))
  }

  private def loadForwardPath(routeId: String): Seq[LatLonD] = {
    val forwardPath = CaseStudy.routeAnalysis(routeId).route.analysis.get.map.forwardPath.get
    val trackPoints = forwardPath.segments.flatMap(_.fragments.map(_.trackPoint))
    trackPoints.map(tp => LatLonD(tp.lat.toDouble, tp.lon.toDouble))
  }

  private def densify(latLons: Seq[LatLonD]): Seq[LatLonD] = {
    val m1 = Haversine.km(47, 14, 48, 14) * 1000
    val m2 = Haversine.km(47, 14, 47, 15) * 1000
    println("m1=" + m1)
    println("m2=" + m2)
    println("meters=" + (m1 / 1201))

    val coordinates = latLons.map(latLon => new Coordinate(latLon.lat, latLon.lon)).toArray
    val lineString = new GeometryFactory().createLineString(coordinates)
    val g: Geometry = Densifier.densify(lineString, 50L / m1)
    g.getCoordinates.toSeq.map(c => LatLonD(c.x, c.y))
  }

  private def deltas(elevations: Seq[Int]): Seq[Int] = {
    elevations.sliding(2).toSeq.map { case Seq(a, b) => b - a }
  }

  private def distances(latLons: Seq[LatLonD]): Seq[Int] = {
    doubleDistances(latLons).map(_.toInt)
  }

  private def totalDistance(latLons: Seq[LatLonD]): Int = {
    doubleDistances(latLons).sum.toInt
  }

  private def doubleDistances(latLons: Seq[LatLonD]): Seq[Double] = {
    latLons.sliding(2).toSeq.map { case Seq(a, b) => Haversine.km(a.lat, a.lon, b.lat, b.lon) * 1000 }
  }

  private def printInfo(title: String, latLons: Seq[LatLonD]): Unit = {
    println(title)
    println(s"  coordinate count: ${latLons.length}")
    println(s"  total distance: ${totalDistance(latLons)}")
    println(s"  distances between coordinates: ${distances(latLons).mkString(", ")}")
  }

  private val segmentDistance = 40

  private def altDensify(latLons: Seq[LatLonD]): Seq[LatLonD] = {
    altDensify1(segmentDistance, latLons, Seq.empty)
  }

  @tailrec
  private def altDensify1(remainingDistanceInSegment: Double, latLons: Seq[LatLonD], result: Seq[LatLonD]): Seq[LatLonD] = {
    if (latLons.size == 1) {
      result ++ latLons
    }
    else {
      val latLon1 = latLons.head
      val latLon2 = latLons(1)
      val segmentLength = distance(latLon1, latLon2)
      if (remainingDistanceInSegment < segmentLength) {
        val ratio = remainingDistanceInSegment / segmentLength
        val lat = latLon1.lat + (ratio * (latLon2.lat - latLon1.lat))
        val lon = latLon1.lon + (ratio * (latLon2.lon - latLon1.lon))
        val latLon = LatLonD(lat, lon)
        val newLatLons = Seq(latLon) ++ latLons.tail
        altDensify1(segmentDistance, newLatLons, result :+ latLon)
      }
      else {
        altDensify1(remainingDistanceInSegment - segmentLength, latLons.tail, result)
      }
    }
  }

  private def distance(a: LatLonD, b: LatLonD): Double = {
    Haversine.km(a.lat, a.lon, b.lat, b.lon) * 1000
  }

  private def gpx(latLons: Seq[LatLonD]): String = {

    val wayPoints: Seq[WayPoint] = latLons.zipWithIndex.map { case (latLon, index) =>
      val name = "" + index
      WayPoint(name, latLon.lat.toString, latLon.lon.toString, "")
    }

    val trackSegments = Seq(GpxSegment(latLons.map(p => TrackPoint(p.lat.toString, p.lon.toString))))

    val file = GpxFile(0L, "", wayPoints, trackSegments)
    new GpxWriter(file).string
  }

}
