package kpn.server.analyzer.engine.monitor

import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString

object MonitorRouteReferenceUtil {

  private val geomFactory = new GeometryFactory

  def toLineStrings(referenceGeoJson: Geometry): Seq[LineString] = {
    val collection = referenceGeoJson match {
      case geometryCollection: GeometryCollection => geometryCollection
      case _ => geomFactory.createGeometryCollection(Array(referenceGeoJson))
    }
    0.until(collection.getNumGeometries).map { index =>
      collection.getGeometryN(index).asInstanceOf[LineString]
    }
  }
}
