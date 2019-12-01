package kpn.server.analyzer.engine.tiles.vector.encoder

import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection

object GeometryUtil {

  def childGeometries(geometryCollection: GeometryCollection): Seq[Geometry] = {
    (0 until geometryCollection.getNumGeometries).map(index => geometryCollection.getGeometryN(index))
  }

}
