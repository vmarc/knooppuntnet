package kpn.server.analyzer.engine.tiles.vector

import kpn.server.analyzer.engine.tiles.PoiTileData
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.vector.encoder.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

import scala.collection.immutable.ListMap

@Component
class PoiVectorTileBuilder {

  def build(data: PoiTileData): Array[Byte] = {
    val geometryFactory = new GeometryFactory
    val encoder = new VectorTileEncoder(Tile.POI_CLIP_BUFFER)
    data.pois.foreach { poi =>
      val x = data.tile.scaleLon(poi.lon)
      val y = data.tile.scaleLat(poi.lat)
      val point = geometryFactory.createPoint(new Coordinate(x, y))
      val userData = ListMap(
        "type" -> poi.elementType,
        "id" -> poi.elementId.toString
      )
      encoder.addPointFeature(poi.layer, userData, point)
    }
    encoder.encode
  }
}
