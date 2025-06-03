package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.core.poi.PoiDefinition
import kpn.server.analyzer.engine.tiles.domain.OldTile

trait OldTileCalculator {

  def tileLonLat(z: Int, lon: Double, lat: Double): OldTile

  def poiTiles(latLon: LatLon, poiDefinitions: Seq[PoiDefinition]): Seq[String]
}
