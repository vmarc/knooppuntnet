package kpn.server.analyzer.engine.tiles

import kpn.api.common.RouteType
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

case class TileAnalysis(
  routeType: RouteType,
  nodes: Seq[TileDataNode],
  routes: Seq[RouteTileInfo]
)
