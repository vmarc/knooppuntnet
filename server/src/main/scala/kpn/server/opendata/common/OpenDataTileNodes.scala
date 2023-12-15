package kpn.server.opendata.common

import kpn.server.analyzer.engine.tiles.domain.OldTile

case class OpenDataTileNodes(
  tile: OldTile,
  nodes: Seq[OpenDataNode]
)
