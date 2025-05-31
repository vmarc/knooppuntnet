package kpn.server.analyzer.engine.tiles

import kpn.api.common.RouteType
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.Tile

// Tile data for all tiles of the given route type and zoom level
case class TilesData(
  routeType: RouteType,
  zoomLevel: Int,
  nodes: Map[String, Seq[NodeTileInfo]],
  routes: Map[String, Seq[RouteTileDoc]]
) {
  def isEmpty: Boolean = {
    nodes.isEmpty && routes.isEmpty
  }

  def tileNames: Seq[String] = {
    val nodeTileNames = nodes.keys
    val routeTileNames = routes.keys
    (nodeTileNames ++ routeTileNames).toSeq.distinct.sorted
  }

  def tileData(tileName: String): TileData = {
    val tile = Tile.routeTileFromName(tileName)
    val nodeTileInfos = nodes.getOrElse(tileName, Seq.empty)
    val routeTileDocs = routes.getOrElse(tileName, Seq.empty)
    TileData(
      routeType,
      tile,
      nodeTileInfos,
      routeTileDocs
    )
  }
}
