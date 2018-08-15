package kpn.core.tiles

import kpn.core.tiles.domain.Tile
import kpn.core.tiles.domain.TileCache
import kpn.shared.LatLon

class NodeTileAnalyzer(tileCache: TileCache) {

  def tiles(z: Int, latLon: LatLon): Seq[Tile] = {

    val lon = latLon.lon
    val lat = latLon.lat

    val x = Tile.x(z, lon)
    val y = Tile.y(z, lat)

    val tile = tileCache(z, x, y)

    Seq(
      Some(tileCache(z, x, y)),
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
    val tile = tileCache(z, x, y)
    if (tile.clipBounds.contains(lon, lat)) Some(tile) else None
  }
}
