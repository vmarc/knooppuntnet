package kpn.core.tiles.vector.encoder

import com.vividsolutions.jts.geom.Geometry

case class VectorTileFeature(geometry: Geometry, tags: Seq[Int]) {
}
