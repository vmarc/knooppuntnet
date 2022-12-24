package kpn.server.analyzer.engine.monitor

import kpn.core.util.Haversine
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.Point
import org.geotools.geometry.jts.JTS
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.GeometryFactory

class CoordinateEnvelopeTest extends UnitTest {

  test("test envelope around point") {
    val essen = new Coordinate(4.46839, 51.46774)
    val zaragoza = new Coordinate(-0.88, 41.65)

    {
      val envelope = new Envelope(essen)
      envelope.expandBy(.04, .02 /* distance in degrees */)
      val width = Haversine.distance(Point(envelope.getMinX, envelope.getMinY), Point(envelope.getMaxX, envelope.getMinY))
      println(width)
      val height = Haversine.distance(Point(envelope.getMinX, envelope.getMinY), Point(envelope.getMinX, envelope.getMaxY))
      println(height)
      println(MonitorRouteAnalysisSupport.toGeoJson(JTS.toGeometry(envelope, new GeometryFactory)))
    }

    {
      val envelope = new Envelope(zaragoza)
      envelope.expandBy(.04, .02 /* distance in degrees */)
      val width = Haversine.distance(Point(envelope.getMinX, envelope.getMinY), Point(envelope.getMaxX, envelope.getMinY))
      println(width)
      val height = Haversine.distance(Point(envelope.getMinX, envelope.getMinY), Point(envelope.getMinX, envelope.getMaxY))
      println(height)
      println(MonitorRouteAnalysisSupport.toGeoJson(JTS.toGeometry(envelope, new GeometryFactory)))
    }
  }
}
