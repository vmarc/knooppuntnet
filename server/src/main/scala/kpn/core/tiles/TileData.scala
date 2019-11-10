package kpn.core.tiles

import kpn.api.common.network.NetworkNodeInfo2
import kpn.api.custom.NetworkType
import kpn.core.tiles.domain.Tile
import kpn.core.tiles.domain.TileRoute

case class TileData(
  networkType: NetworkType,
  tile: Tile,
  nodes: Seq[NetworkNodeInfo2],
  routes: Seq[TileRoute]
)
