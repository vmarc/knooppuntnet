package kpn.server.analyzer.engine.tile

import kpn.api.custom.NetworkType

object TileTask {

  val prefix: String = "tile-task:"

  def task(tileName: String): String = {
    s"${TileTask.prefix}$tileName"
  }

  def fullTileName(task: String): String = {
    task.substring(prefix.length)
  }

  def networkType(task: String): NetworkType = {
    if (task.contains("horse-riding")) {
      NetworkType.horseRiding
    }
    else if (task.contains("inline-skating")) {
      NetworkType.inlineSkating
    }
    else {
      NetworkType.withName(fullTileName(task).split("-").head).get
    }
  }

  def tileName(task: String): String = {
    val n = fullTileName(task)
    n.substring(networkType(task).name.length + 1)
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
