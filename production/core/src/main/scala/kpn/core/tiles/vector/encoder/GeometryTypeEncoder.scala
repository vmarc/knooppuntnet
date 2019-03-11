package kpn.core.tiles.vector.encoder

import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.geom.MultiPoint
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.Polygon
import kpn.core.tiles.vector.ProtobufVectorTile

object GeometryTypeEncoder {

  def encode(geometry: Geometry): Int = {
    geometry match {
      case g: Point => ProtobufVectorTile.Tile.POINT
      case g: MultiPoint => ProtobufVectorTile.Tile.POINT
      case g: LineString => ProtobufVectorTile.Tile.LINESTRING
      case g: MultiLineString => ProtobufVectorTile.Tile.LINESTRING
      case g: Polygon => ProtobufVectorTile.Tile.POLYGON
      case _ => ProtobufVectorTile.Tile.UNKNOWN
    }
  }
}
