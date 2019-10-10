package kpn.core.tiles.vector.encoder

import org.locationtech.jts.geom.Geometry

case class VectorTileFeature(geometry: Geometry, tags: Seq[Int])
