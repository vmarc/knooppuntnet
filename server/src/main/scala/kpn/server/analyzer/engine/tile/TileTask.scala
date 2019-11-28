package kpn.server.analyzer.engine.tile

import kpn.api.custom.NetworkType

object TileTask {

  val prefix: String = "tile-task:"

  def fullTileName(task: String): String = {
    task.substring(prefix.length)
  }

  def networkType(task: String): NetworkType = {
    NetworkType.withName(fullTileName(task).split("-").head).get
  }

  def tileName(task: String): String = {
    val n = fullTileName(task)
    n.substring(n.indexOf("-") + 1)
  }

  def zoomLevel(task: String): Int = {
    task.split("-")(2).toInt
  }

}