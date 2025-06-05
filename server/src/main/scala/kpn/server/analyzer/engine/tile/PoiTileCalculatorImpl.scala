package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.api.common.tiles.ZoomLevel
import kpn.core.poi.PoiDefinition
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.PoiTileCache
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.springframework.stereotype.Component

@Component
class PoiTileCalculatorImpl extends PoiTileCalculator {

  private val cache = new PoiTileCache()

  def tileLonLat(z: Int, lon: Double, lat: Double): Tile = {
    val worldX = lonToWorldX(lon)
    val worldY = latToWorldY(lat)

    val x = Tile.tileX(z, worldX)
    val y = Tile.tileY(z, worldY)
    val tileName = s"$z-$x-$y"

    cache(tileName)
  }

  private def tileXY(z: Int, x: Int, y: Int): Tile = {
    val tileName = s"$z-$x-$y"
    cache(tileName)
  }

  def poiTiles(latLon: LatLon, poiDefinitions: Seq[PoiDefinition]): Seq[String] = {
    val minLevel = poiDefinitions.map(_.minLevel).min
    val tiles = (minLevel.toInt to ZoomLevel.vectorTileMaxZoom).flatMap { z =>

      //TODO redesign tiles - re-use logic from NodeTileCalculator here???
      val lon = latLon.lon
      val lat = latLon.lat

      val tile = tileLonLat(z, lon, lat)

      Seq(
        Some(tile),
        explore(lon, lat, z, tile.x - 1, tile.y),
        explore(lon, lat, z, tile.x + 1, tile.y),
        explore(lon, lat, z, tile.x - 1, tile.y - 1),
        explore(lon, lat, z, tile.x, tile.y - 1),
        explore(lon, lat, z, tile.x + 1, tile.y - 1),
        explore(lon, lat, z, tile.x - 1, tile.y + 1),
        explore(lon, lat, z, tile.x, tile.y + 1),
        explore(lon, lat, z, tile.x + 1, tile.y + 1)
      ).flatten
    }
    tiles.map(_.name)
  }

  private def explore(lon: Double, lat: Double, z: Int, x: Int, y: Int): Option[Tile] = {
    val tileName = s"$z-$x-$y"
    val tile = cache(tileName)
    if (tile.clipBounds.contains(lon, lat)) Some(tile) else None
  }
}
