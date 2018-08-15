package kpn.core.tiles.domain

import kpn.shared.network.NetworkNodeInfo2

case class TileNodes(tile: Tile, nodes: Seq[NetworkNodeInfo2])
