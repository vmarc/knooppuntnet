package kpn.server.analyzer.engine.tiles

import kpn.api.common.network.NetworkInfoNode
import kpn.api.custom.NetworkType
import kpn.core.mongo.doc.NodeDoc
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

trait TileDataNodeBuilder {

  def build(networkType: NetworkType, node: NodeDoc): Option[TileDataNode]

  def build(networkType: NetworkType, node: NetworkInfoNode): Option[TileDataNode]

}
