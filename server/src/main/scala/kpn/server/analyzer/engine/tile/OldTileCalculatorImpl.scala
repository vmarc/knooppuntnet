package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.api.common.tiles.ZoomLevel
import kpn.core.poi.PoiDefinition
import kpn.server.analyzer.engine.tiles.domain.OldTile
import kpn.server.analyzer.engine.tiles.domain.OldTileCache
import org.springframework.stereotype.Component

@Component
class OldTileCalculatorImpl extends OldTileCalculator {

  private val cache = new OldTileCache()

  def tileLonLat(z: Int, lon: Double, lat: Double): OldTile = {
    cache(z, lon, lat)
  }

  def tileXY(z: Int, x: Int, y: Int): OldTile = {
    cache(z, x, y)
  }

  def tileNamed(tileName: String): OldTile = {
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

  private def explore(lon: Double, lat: Double, z: Int, x: Int, y: Int): Option[OldTile] = {
    val tile = tileXY(z, x, y)
    if (tile.poiClipBounds.contains(lon, lat)) Some(tile) else None
  }

}
