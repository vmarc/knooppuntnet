package kpn.server.repository

import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.tiles.domain.Tile

trait TileRepository {

  def nodeIds(networkType: NetworkType, tile: Tile): Seq[Long]

  def routeIds(networkType: NetworkType, tile: Tile): Seq[Long]

}
