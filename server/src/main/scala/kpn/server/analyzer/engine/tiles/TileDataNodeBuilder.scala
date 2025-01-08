package kpn.server.analyzer.engine.tiles

import kpn.api.common.RouteType
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

trait TileDataNodeBuilder {

  def build(routeType: RouteType, node: NodeTileInfo): Option[TileDataNode]
}
