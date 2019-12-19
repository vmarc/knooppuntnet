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

  def tiles(latLon: LatLon, poiDefinitions: Seq[PoiDefinition]): Seq[String] = {
    val minLevel = poiDefinitions.map(_.minLevel).min
    val tiles = (minLevel.toInt to ZoomLevel.poiTileMaxZoom).map(z => tileLonLat(z, latLon.lon, latLon.lat))
    tiles.map(_.name)
  }
}
