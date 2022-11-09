package kpn.core.tools.monitor

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory

import java.io.File
import scala.xml.Elem
import scala.xml.XML

object MonitorRouteGpxReader {
  def main(args: Array[String]): Unit = {
    val result = new MonitorRouteGpxReader().readFile("/share/fotos/GR59.gpx")
    println(result.getLength)
  }
}

class MonitorRouteGpxReader {

  private val geomFactory = new GeometryFactory

  def readFile(filename: String): GeometryCollection = {
    read(XML.loadFile(new File(filename)))
  }

  def readString(xmlString: String): GeometryCollection = {
    read(XML.loadString(xmlString))
  }

  def read(xml: Elem): GeometryCollection = {
    val tracks = (xml \ "trk").flatMap { trk =>
      (trk \ "trkseg").map { trkseg =>
        val coordinates = (trkseg \ "trkpt").map { trkpt =>
          val lat = (trkpt \ "@lat").text
          val lon = (trkpt \ "@lon").text
          new Coordinate(lon.toDouble, lat.toDouble)
        }
        geomFactory.createLineString(coordinates.toArray)
      }
    }
    geomFactory.createGeometryCollection(tracks.toArray)
  }
}
