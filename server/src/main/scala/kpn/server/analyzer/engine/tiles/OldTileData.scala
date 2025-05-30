package kpn.server.analyzer.engine.tiles

import kpn.api.common.RouteType
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute

case class OldTileData(
  routeType: RouteType,
  nodes: Seq[TileDataNode],
  routes: Seq[TileDataRoute]
) {
  def isEmpty: Boolean = {
    nodes.isEmpty && routes.isEmpty
  }
}
