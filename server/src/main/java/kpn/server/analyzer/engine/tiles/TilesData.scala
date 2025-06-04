package kpn.server.analyzer.engine.tiles

import kpn.api.common.RouteType
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileContext
import kpn.server.analyzer.engine.tiles.domain.TileId

// Tile data for all tiles of the given route type and zoom level
case class TilesData(
  routeType: RouteType,
  zoomLevel: Int,
  nodeTileInfos: Map[String, Seq[NodeTileInfo]],
  routeTileInfos: Map[String, Seq[RouteTileInfo]]
) {
  def isEmpty: Boolean = {
    nodeTileInfos.isEmpty && routeTileInfos.isEmpty
  }

  def tileNames: Seq[String] = {
    val nodeTileNames = nodeTileInfos.keys
    val routeTileNames = routeTileInfos.keys
    (nodeTileNames ++ routeTileNames).toSeq.distinct.sorted
  }

  def tileData(tileName: String): TileData = {
    val tileContext = TileContext.route(zoomLevel)
    val tile = tileContext.tile(TileId(tileName))
    val tileNodeTileInfos = nodeTileInfos.getOrElse(tileName, Seq.empty)
    val tileRouteTileInfos = routeTileInfos.getOrElse(tileName, Seq.empty)
    TileData(
      routeType,
      tile,
      tileNodeTileInfos,
      tileRouteTileInfos
    )
  }
}
