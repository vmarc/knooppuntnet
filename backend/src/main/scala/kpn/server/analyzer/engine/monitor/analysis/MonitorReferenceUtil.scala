package kpn.server.analyzer.engine.monitor.analysis

import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString

object MonitorReferenceUtil {

  private val geometryFactory = new GeometryFactory

  def toLineStrings(referenceGeometry: Geometry): Seq[LineString] = {
    referenceGeometry match {
      case lineString: LineString => Seq(lineString)
      case geometryCollection: GeometryCollection =>
        0.until(geometryCollection.getNumGeometries).map { index =>
          geometryCollection.getGeometryN(index).asInstanceOf[LineString]
        }
      case _ =>
        throw new IllegalArgumentException(s"Unexpected geometry type: ${referenceGeometry.getClass.getSimpleName}")
    }
  }
}
