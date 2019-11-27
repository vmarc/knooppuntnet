package kpn.core.tiles

import kpn.api.custom.NetworkType
import kpn.core.tiles.domain.Tile
import kpn.core.tiles.domain.TileDataNode
import kpn.core.tiles.domain.TileDataRoute

case class TileData(
  networkType: NetworkType,
  tile: Tile,
  nodes: Seq[TileDataNode],
  routes: Seq[TileDataRoute]
)
