package kpn.server.analyzer.engine.tiles.vector

import kpn.server.analyzer.engine.tiles.PoiTileData
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.PoiTiles
import no.ecc.vectortile.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class PoiVectorTileBuilder {
  private val geometryFactory = new GeometryFactory

  def build(data: PoiTileData): Array[Byte] = {
    val extent = PoiTiles.extent(data.tile.z)
    val clipBufferSize = PoiTiles.clipBufferSize(data.tile.z)

    val encoder = new VectorTileEncoder(extent, clipBufferSize, false)
    data.pois.foreach { poi =>
      val worldCoordinate = new Coordinate(lonToWorldX(poi.lon), latToWorldY(poi.lat))
      val coordinate = PoiTiles.toTileCoordinate(data.tile, worldCoordinate)
      val coordinateInt = new Coordinate(coordinate.x.toInt, coordinate.y.toInt)
      val point = geometryFactory.createPoint(coordinateInt)
      val userData = new java.util.HashMap[String, String]()
      userData.put("type", poi.elementType)
      userData.put("id", poi.elementId.toString)
      encoder.addFeature(poi.layer, userData, point)
    }
    encoder.encode
  }
}
