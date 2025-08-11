package kpn.server.analyzer.engine.tile

import kpn.api.common.RouteType

object TileTask {

  val prefix: String = "tile-task:"

  def task(tileName: String): String = {
    s"${TileTask.prefix}$tileName"
  }

  def fullTileName(task: String): String = {
    task.substring(prefix.length)
  }

  def routeType(task: String): RouteType = {
    if (task.contains("horse-riding")) {
      RouteType.horseRiding
    }
    else if (task.contains("inline-skating")) {
      RouteType.inlineSkating
    }
    else {
      RouteType.valueOf(fullTileName(task).split("-").head)
    }
  }

  def tileName(task: String): String = {
    val n = fullTileName(task)
    n.substring(routeType(task).toString.length + 1)
  }

  def zoomLevel(task: String): Int = {
    if (task.contains("horse-riding") || task.contains("inline-skating")) {
      task.split("-")(3).toInt
    }
    else {
      task.split("-")(2).toInt
    }
  }
}
