package kpn.server.analyzer.engine.tiles

import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

trait TileDataNodeBuilder {

  def build(networkType: NetworkType, node: NodeTileInfo): Option[TileDataNode]

}
