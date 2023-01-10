package kpn.server.analyzer.engine.monitor

import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString

object MonitorRouteReferenceUtil {

  private val geometryFactory = new GeometryFactory

  def toLineStrings(referenceGeometry: Geometry): Seq[LineString] = {
    val collection = referenceGeometry match {
      case geometryCollection: GeometryCollection => geometryCollection
      case _ => geometryFactory.createGeometryCollection(Array(referenceGeometry))
    }
    0.until(collection.getNumGeometries).map { index =>
      collection.getGeometryN(index).asInstanceOf[LineString]
    }
  }
}
