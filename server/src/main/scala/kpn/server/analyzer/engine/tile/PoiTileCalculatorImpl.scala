package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.core.poi.PoiDefinition
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.PoiTileCache
import kpn.server.analyzer.engine.tiles.domain.PointTileCalculator
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.springframework.stereotype.Component

@Component
class PoiTileCalculatorImpl extends PointTileCalculator(new PoiTileCache()) with PoiTileCalculator {

  def tileLonLat(z: Int, lon: Double, lat: Double): Tile = {
    val worldX = lonToWorldX(lon)
    val worldY = latToWorldY(lat)

    val x = Tile.tileX(z, worldX)
    val y = Tile.tileY(z, worldY)
    val tileName = s"$z-$x-$y"

    tileCache(tileName)
  }

  private def tileXY(z: Int, x: Int, y: Int): Tile = {
    val tileName = s"$z-$x-$y"
    tileCache(tileName)
  }

  def poiTiles(latLon: LatLon, poiDefinitions: Seq[PoiDefinition]): Seq[String] = {
    val minLevel = poiDefinitions.map(_.minLevel).min
    val tilesX = (minLevel.toInt to ZoomLevel.poiTileMaxZoom).flatMap { z =>

      tiles(z, latLon).map(_.name)
    }
    tilesX.distinct.sorted
  }
}
