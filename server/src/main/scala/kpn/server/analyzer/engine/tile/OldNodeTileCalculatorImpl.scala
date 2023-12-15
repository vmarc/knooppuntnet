package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.server.analyzer.engine.tiles.domain.OldTile
import org.springframework.stereotype.Component

@Component
class OldNodeTileCalculatorImpl(tileCalculator: OldTileCalculator) extends OldNodeTileCalculator {

  override def tiles(z: Int, latLon: LatLon): Seq[OldTile] = {

    val lon = latLon.lon
    val lat = latLon.lat

    val x = OldTile.x(z, lon)
    val y = OldTile.y(z, lat)

    Seq(
      Some(tileCalculator.tileXY(z, x, y)),
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

  private def explore(lon: Double, lat: Double, z: Int, x: Int, y: Int): Option[OldTile] = {
    val tile = tileCalculator.tileXY(z, x, y)
    if (tile.clipBounds.contains(lon, lat)) Some(tile) else None
  }
}
