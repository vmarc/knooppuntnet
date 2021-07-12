package kpn.server.analyzer.engine.tiles

import kpn.api.common.NodeInfo
import kpn.api.common.network.NetworkInfoNode
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

trait TileDataNodeBuilder {

  def build(networkType: NetworkType, node: NodeInfo): Option[TileDataNode]

  def build(networkType: NetworkType, node: NetworkInfoNode): Option[TileDataNode]

}
