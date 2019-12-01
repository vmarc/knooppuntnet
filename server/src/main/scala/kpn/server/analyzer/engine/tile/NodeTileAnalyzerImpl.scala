package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.springframework.stereotype.Component

@Component
class NodeTileAnalyzerImpl(tileCalculator: TileCalculator) extends NodeTileAnalyzer {

  override def tiles(z: Int, latLon: LatLon): Seq[Tile] = {

    val lon = latLon.lon
    val lat = latLon.lat

    val x = Tile.x(z, lon)
    val y = Tile.y(z, lat)

    val tile = tileCalculator.get(z, x, y)

    Seq(
      Some(tileCalculator.get(z, x, y)),
      explore(lon, lat, z, x - 1, y),
      explore(lon, lat, z, x + 1, y),
      explore(lon, lat, z, x - 1, y - 1),
      explore(lon, lat, z, x, y - 1),
      explore(lon, lat, z, x + 1, y - 1),
      explore(lon, lat, z, x - 1, y + 1),
      explore(lon, lat, z, x, y + 1),
      explore(lon, lat, z, x + 1, y + 1)
    ).flatten
  }

  private def explore(lon: Double, lat: Double, z: Int, x: Int, y: Int): Option[Tile] = {
    val tile = tileCalculator.get(z, x, y)
    if (tile.clipBounds.contains(lon, lat)) Some(tile) else None
  }
}
