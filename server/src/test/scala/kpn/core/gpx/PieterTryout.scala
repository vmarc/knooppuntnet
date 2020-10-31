package kpn.core.gpx

import java.io.File

import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy
import org.locationtech.jts.densify.Densifier
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.io.geojson.GeoJsonWriter

import scala.xml.XML

object PieterTryout {
  def main(args: Array[String]): Unit = {
    new PieterTryout().run()
  }
}

class PieterTryout {

  private val geomFactory = new GeometryFactory
  private val sampleDistanceMeters = 10
  private val toleranceMeters = 10

  def run(): Unit = {

    val gpx = readGpx()
    val osm = readOsmRelation()

    println(s"gpx length=${toMeters(gpx.getLength)}")
    println(s"osm length=${toMeters(osm.getLength)}")

    val distanceBetweenSamples = sampleDistanceMeters.toDouble * osm.getLength / toMeters(osm.getLength)
    val densifiedOsm = Densifier.densify(osm, distanceBetweenSamples)
    val sampleCoordinates = densifiedOsm.getCoordinates.toSeq

    val distances = sampleCoordinates.toList.map(coordinate => toMeters(gpx.distance(geomFactory.createPoint(coordinate))))

    println(s"distance max=${distances.max}")
    println(s"distance min=${distances.min}")

    val withinTolerance = distances.map(distance => distance < toleranceMeters)
    val okAndIndexes = withinTolerance.zipWithIndex.map { case (ok, index) => ok -> index }
    val splittedOkAndIndexes = split(okAndIndexes)

    val ok: MultiLineString = toMultiLineString(sampleCoordinates, splittedOkAndIndexes.filter(_.head._1))
    val nok = toMultiLineString(sampleCoordinates, splittedOkAndIndexes.filterNot(_.head._1))

    println(new GeoJsonWriter().write(gpx))
    println(new GeoJsonWriter().write(ok))
    println(new GeoJsonWriter().write(nok))

    println(gpx.getCoordinates.map(_.getX).min)
    println(gpx.getCoordinates.map(_.getX).max)
    println(gpx.getCoordinates.map(_.getY).min)
    println(gpx.getCoordinates.map(_.getY).max)
  }

  private def toMultiLineString(sampleCoordinates: Seq[Coordinate], segments: List[List[(Boolean, Int)]]) = {
    geomFactory.createMultiLineString(segments.map(segment => toLineString(sampleCoordinates, segment)).toArray)
  }

  private def split(list: List[(Boolean, Int)]): List[List[(Boolean, Int)]] = {
    list match {
      case Nil => Nil
      case head :: tail =>
        val segment = list.takeWhile(_._1 == head._1)
        segment +: split(list.drop(segment.length))
    }
  }

  private def readGpx(): LineString = {

    val xml = XML.loadFile(new File("/kpn/example.gpx"))
    val tracks = (xml \ "trk").map { trk =>
      (trk \ "trkseg").map { trkseg =>
        val coordinates = (trkseg \ "trkpt").map { trkpt =>
          val lat = (trkpt \ "@lat").text
          val lon = (trkpt \ "@lon").text
          new Coordinate(lon.toDouble, lat.toDouble)
        }
        geomFactory.createLineString(coordinates.toArray)
      }
    }
    if (tracks.size != 1) {
      throw new RuntimeException("Unexpected number of tracks in gpx file: " + tracks.size)
    }
    val track = tracks.head
    if (track.size != 1) {
      throw new RuntimeException("Unexpected number of track segments in gpx file: " + track.size)
    }
    track.head
  }

  private def readOsmRelation(): LineString = {
    val route = CaseStudy.routeAnalysis("route-3945543").route
    val coordinates = route.analysis.map.forwardPath.get.trackPoints.map { trackPoint =>
      new Coordinate(trackPoint.lon.toDouble, trackPoint.lat.toDouble)
    }
    geomFactory.createLineString(coordinates.toArray)
  }

  private def toMeters(value: Double): Double = {
    value * (math.Pi / 180) * 6378137
  }

  private def toLineString(osmCoordinates: Seq[Coordinate], segment: List[(Boolean, Int)]): LineString = {
    val indexes = segment.map(_._2)
    val coordinates = indexes.map(index => osmCoordinates(index))
    geomFactory.createLineString(coordinates.toArray)
  }

}
