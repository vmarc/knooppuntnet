package kpn.server.analyzer.engine.tiles

import kpn.api.common.RouteType
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.Tile

case class TileData(
  routeType: RouteType,
  tile: Tile,
  nodes: Seq[NodeTileInfo],
  routes: Seq[RouteTileDoc]
)
