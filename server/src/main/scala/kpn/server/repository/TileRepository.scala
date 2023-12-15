package kpn.server.repository

import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.tiles.domain.OldTile

trait TileRepository {

  def nodeIds(networkType: NetworkType, tile: OldTile): Seq[Long]

  def routeIds(networkType: NetworkType, tile: OldTile): Seq[Long]

}
