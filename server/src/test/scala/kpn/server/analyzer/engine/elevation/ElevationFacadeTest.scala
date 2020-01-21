package kpn.server.analyzer.engine.elevation

import kpn.api.common.common.TrackPoint
import kpn.core.gpx.GpxFile
import kpn.core.gpx.GpxSegment
import kpn.core.gpx.GpxWriter
import kpn.core.gpx.WayPoint
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy
import kpn.server.analyzer.engine.tiles.domain.Point
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ElevationFacadeTest extends FunSuite with Matchers {

  test("route elevation profile") {

    // load route 10460202 50-53 Austria
    // See: https://cycling.waymarkedtrails.org/#route?id=10460202&map=15!47.2252!14.777
    // length 6.01 km
    // expected accumulated ascent: 85 m
    // expected accumulated descent: 100 m

    val forwardPath = loadForwardPath("10460202")
    val sampleCoordinates = new ElevationCoordinatesBuilder().build(forwardPath)

    printInfo("original", forwardPath)
    printInfo("sampleCoordinates", sampleCoordinates)

    val repo = new ElevationRepositoryImpl()

    // calculate elevations first time to warm up the tile cache
    sampleCoordinates.flatMap(repo.elevation)

    val t1 = System.currentTimeMillis()
    val elevations = sampleCoordinates.flatMap(repo.elevation)
    val t2 = System.currentTimeMillis()
    println(s"elevations calculated in ${t2 - t1}ms")

    val ascending = deltas(elevations).filter(_ > 0).sum
    val descending = 0 - deltas(elevations).filter(_ < 0).sum
    println(s"ascending: ${ascending}m")
    println(s"descending: ${descending}m")

    println(elevations)
    // elevations.reverse.foreach(println)

    println(gpx(sampleCoordinates))
  }

  private def loadForwardPath(routeId: String): Seq[Point] = {
    val forwardPath = CaseStudy.routeAnalysis(routeId).route.analysis.get.map.forwardPath.get
    val trackPoints = forwardPath.segments.flatMap(_.fragments.map(_.trackPoint))
    trackPoints.map(tp => Point(tp.lat.toDouble, tp.lon.toDouble))
  }

  private def deltas(elevations: Seq[Int]): Seq[Int] = {
    elevations.sliding(2).toSeq.map { case Seq(a, b) => b - a }
  }

  private def distances(points: Seq[Point]): Seq[Int] = {
    doubleDistances(points).map(_.toInt)
  }

  private def totalDistance(points: Seq[Point]): Int = {
    doubleDistances(points).sum.toInt
  }

  private def doubleDistances(points: Seq[Point]): Seq[Double] = {
    points.sliding(2).toSeq.map { case Seq(a, b) => Haversine.km(a.x, a.y, b.x, b.y) * 1000 }
  }

  private def printInfo(title: String, points: Seq[Point]): Unit = {
    println(title)
    println(s"  coordinate count: ${points.length}")
    println(s"  total distance: ${totalDistance(points)}")
    println(s"  distances between coordinates: ${distances(points).mkString(", ")}")
  }

  private def gpx(points: Seq[Point]): String = {
    val wayPoints: Seq[WayPoint] = points.zipWithIndex.map { case (point, index) =>
      val name = "" + index
      WayPoint(name, point.x.toString, point.y.toString, "")
    }
    val trackSegments = Seq(GpxSegment(points.map(p => TrackPoint(p.x.toString, p.y.toString))))
    val file = GpxFile(0L, "", wayPoints, trackSegments)
    new GpxWriter(file).string
  }

}
