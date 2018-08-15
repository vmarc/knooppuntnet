package kpn.core.tiles.vector.encoder

import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.LineString
import com.vividsolutions.jts.geom.MultiLineString
import com.vividsolutions.jts.geom.MultiPoint
import com.vividsolutions.jts.geom.Point
import com.vividsolutions.jts.geom.Polygon
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
