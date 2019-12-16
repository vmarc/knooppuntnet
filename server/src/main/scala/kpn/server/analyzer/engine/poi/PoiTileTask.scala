package kpn.server.analyzer.engine.poi

import kpn.server.analyzer.engine.tiles.domain.Tile

object PoiTileTask {

  val prefix: String = "poi-tile-task:"

  def tileName(task: String): String = {
    task.substring(prefix.length)
  }

  def withTile(tile: Tile): String = {
    withTileName(tile.name)
  }

  def withTileName(tileName: String): String = {
    s"$prefix$tileName"
  }

}
