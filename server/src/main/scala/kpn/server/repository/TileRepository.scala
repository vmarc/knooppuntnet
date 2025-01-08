package kpn.server.repository

import kpn.api.common.RouteType
import kpn.server.analyzer.engine.tiles.domain.Tile

trait TileRepository {

  def nodeIds(routeType: RouteType, tile: Tile): Seq[Long]

  def routeIds(routeType: RouteType, tile: Tile): Seq[Long]
}
