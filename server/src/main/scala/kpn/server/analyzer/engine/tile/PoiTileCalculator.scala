package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.core.poi.PoiDefinition
import kpn.server.analyzer.engine.tiles.domain.Tile

trait PoiTileCalculator {

  def tileLonLat(z: Int, lon: Double, lat: Double): Tile

  def poiTiles(latLon: LatLon, poiDefinitions: Seq[PoiDefinition]): Seq[String]
}
