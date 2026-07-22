package kpn.server.opendata.common

import kpn.server.analyzer.engine.tiles.domain.Tile

case class OpenDataTileNodes(
  tile: Tile,
  nodes: Seq[OpenDataNode]
)
