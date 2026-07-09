package kpn.server.analyzer.engine.tiles

import kpn.api.common.RouteType
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.Tile

case class TileData(
  routeType: RouteType,
  tile: Tile,
  nodeTileInfos: Seq[NodeTileInfo],
  routeTileInfos: Seq[RouteTileInfo]
)
