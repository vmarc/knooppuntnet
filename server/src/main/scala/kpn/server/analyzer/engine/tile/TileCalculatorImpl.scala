package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.api.common.tiles.ZoomLevel
import kpn.core.poi.PoiDefinition
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileCache
import org.springframework.stereotype.Component

@Component
class TileCalculatorImpl extends TileCalculator {

  private val cache = new TileCache()

  def tileLonLat(z: Int, lon: Double, lat: Double): Tile = {
    cache(z, lon, lat)
  }

  def tileXY(z: Int, x: Int, y: Int): Tile = {
    cache(z, x, y)
  }

  def tileNamed(tileName: String): Tile = {
    cache(tileName)
  }

  def poiTiles(latLon: LatLon, poiDefinitions: Seq[PoiDefinition]): Seq[String] = {
    val minLevel = poiDefinitions.map(_.minLevel).min
    val tiles = (minLevel.toInt to ZoomLevel.poiTileMaxZoom).flatMap { z =>
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
    val tile = tileXY(z, x, y)
    if (tile.poiClipBounds.contains(lon, lat)) Some(tile) else None
  }

}
