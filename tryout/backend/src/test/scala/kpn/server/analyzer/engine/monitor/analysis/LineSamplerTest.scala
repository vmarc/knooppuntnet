package kpn.server.analyzer.engine.monitor.analysis

import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.Haversine
import kpn.core.util.UnitTest
import org.locationtech.jts.geom.LineString

import scala.xml.XML

class LineSamplerTest extends UnitTest {

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

    val sampleCoordinates = LineSampler.toSampleCoordinates(10, lineString)

    val distances = sampleCoordinates.sliding(2).toSeq.map { case Seq(c1, c2) =>
      Haversine.km(c1.y, c1.x, c2.y, c2.x) * 1000
    }

    Haversine.meters(lineString).toInt should equal(181)
    distances.sum.toInt should equal(181)
  }
}
