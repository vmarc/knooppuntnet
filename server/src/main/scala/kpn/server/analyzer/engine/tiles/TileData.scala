package kpn.server.analyzer.engine.tiles

import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute

case class TileData(
  networkType: NetworkType,
  tile: Tile,
  nodes: Seq[TileDataNode],
  routes: Seq[TileDataRoute]
)
