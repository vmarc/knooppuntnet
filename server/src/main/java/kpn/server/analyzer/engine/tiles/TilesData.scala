package kpn.server.analyzer.engine.tiles

import kpn.api.common.RouteType
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.Tile

// Tile data for all tiles of the given route type and zoom level
case class TilesData(
  routeType: RouteType,
  zoomLevel: Int,
  nodes: Seq[NodeTileInfo],
  routes: Seq[RouteTileDoc]
) {
  def isEmpty: Boolean = {
    nodes.isEmpty && routes.isEmpty
  }

  def tileNames: Seq[String] = {
    val nodeTileNames = nodes.map(_.tileName)
    val routeTileNames = routes.map(_.tileName)
    (nodeTileNames ++ routeTileNames).distinct.sorted
  }

  def tileData(tileName: String): TileData = {
    val tile = Tile.routeTileFromName(tileName)
    val nodeTileInfos = nodes.filter(_.tileName == tileName)
    val routeTileDocs = routes.filter(_.tileName == tileName)
    TileData(
      routeType,
      tile,
      nodeTileInfos,
      routeTileDocs
    )
  }
}
