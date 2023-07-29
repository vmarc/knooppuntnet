package kpn.server.analyzer.engine.monitor

import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.Haversine
import kpn.core.util.UnitTest
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString

import scala.xml.XML

class MonitorRouteAnalysisSupportTest extends UnitTest {

  test("first densify, then simplify again") {

    val coordinate1 = new Coordinate(51.4633666, 4.4553911)
    val coordinate2 = new Coordinate(51.4618272, 4.4562458)
    val coordinate3 = new Coordinate(51.4614496, 4.4550560)

    val geometryFactory = new GeometryFactory
    val originalLineString = geometryFactory.createLineString(Array(coordinate1, coordinate2, coordinate3))

    val coordinates = MonitorRouteAnalysisSupport.toSampleCoordinates(10, originalLineString)

    coordinates.size should equal(35)

    val sequence = ReferenceCoordinateSequence(0.until(35))
    val newLineString = MonitorRouteAnalysisSupport.toLineString(coordinates, sequence)

    newLineString.getCoordinates should equal(Array(coordinate1, coordinate2, coordinate3))
  }

  test("toSampleCoordinates") {

    val gpx =
      """
        |<gpx>
        |  <trk>
        |    <trkseg>
        |      <trkpt lat="51.4633666" lon="4.4553911"></trkpt>
        |      <trkpt lat="51.4618272" lon="4.4562458"></trkpt>
        |    </trkseg>
        |  </trk>
        |</gpx>
        |""".stripMargin

    val xml = XML.loadString(gpx)
    val geometry = new MonitorRouteGpxReader().read(xml)
    val lineString = geometry.getGeometryN(0).asInstanceOf[LineString]

    val sampleCoordinates = MonitorRouteAnalysisSupport.toSampleCoordinates(10, lineString)

    val distances = sampleCoordinates.sliding(2).toSeq.map { case Seq(c1, c2) =>
      Haversine.km(c1.y, c1.x, c2.y, c2.x) * 1000
    }

    Haversine.meters(lineString).toInt should equal(181)
    distances.sum.toInt should equal(181)
  }
}
