package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.core.poi.PoiDefinition
import kpn.server.analyzer.engine.tiles.domain.Tile

trait TileCalculator {

  def tileLonLat(z: Int, lon: Double, lat: Double): Tile

  def tileXY(z: Int, x: Int, y: Int): Tile

  def tileNamed(tileName: String): Tile

  def poiTiles(latLon: LatLon, poiDefinitions: Seq[PoiDefinition]): Seq[String]

}
