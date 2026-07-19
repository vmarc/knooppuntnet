package kpn.core.util

import kpn.api.common.LatLon
import kpn.api.common.LatLonImpl
import kpn.core.tools.monitor.MonitorRouteGpxReader
import org.locationtech.jts.geom.LineString

import scala.xml.XML

class HaversineTest extends UnitTest {

  private val lat1 = 51.4633666
  private val lon1 = 4.4553911
  private val lat2 = 51.4618272
  private val lon2 = 4.4562458

  test("km") {
    val km = Haversine.km(lat1, lon1, lat2, lon2)
    (km * 1000).toInt should equal(181)
  }

  test("meters from nodes") {

    val nodes: Seq[LatLon] = Seq(
      LatLonImpl(lat1.toString, lon1.toString),
      LatLonImpl(lat2.toString, lon2.toString)
    )

    Haversine.meters(nodes) should equal(181)
  }

  test("meters from lineString") {

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

    Haversine.meters(lineString).toInt should equal(181)
  }
}
