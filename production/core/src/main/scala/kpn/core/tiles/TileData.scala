package kpn.core.tiles

import kpn.core.tiles.domain.Tile
import kpn.core.tiles.domain.TileRoute
import kpn.shared.NetworkType
import kpn.shared.network.NetworkNodeInfo2

case class TileData(
  networkType: NetworkType,
  tile: Tile,
  nodes: Seq[NetworkNodeInfo2],
  routes: Seq[TileRoute]
)
