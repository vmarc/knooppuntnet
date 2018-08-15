package kpn.core.tiles.vector.encoder

import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.GeometryCollection

object GeometryUtil {

  def childGeometries(geometryCollection: GeometryCollection): Seq[Geometry] = {
    (0 until geometryCollection.getNumGeometries).map(index => geometryCollection.getGeometryN(index))
  }

}
