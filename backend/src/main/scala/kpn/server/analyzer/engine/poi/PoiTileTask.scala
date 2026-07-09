package kpn.server.analyzer.engine.poi

object PoiTileTask {

  val prefix: String = "poi-tile-task:"

  def tileName(task: String): String = {
    task.substring(prefix.length)
  }

  def withTileName(tileName: String): String = {
    s"$prefix$tileName"
  }
}
