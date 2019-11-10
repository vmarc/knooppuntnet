package kpn.core.tiles.domain

import kpn.api.common.network.NetworkNodeInfo2

case class TileNodes(tile: Tile, nodes: Seq[NetworkNodeInfo2])
