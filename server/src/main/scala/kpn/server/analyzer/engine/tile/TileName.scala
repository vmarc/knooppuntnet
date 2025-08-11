package kpn.server.analyzer.engine.tile

import kpn.api.common.RouteType

object TileName {

  def routeType(tileName: String): String = {
    if (tileName.contains("horse-riding")) {
      "horse-riding"
    }
    else if (tileName.contains("inline-skating")) {
      "inline-skating"
    }
    else {
      tileName.split("-").head
    }
  }

  def tileNumber(tileName: String): String = {
    tileName.substring(routeType(tileName).length + 1).replaceAll("-", "/")
  }

  def tileZoomLevel(tileName: String): Int = {
    tileName.substring(routeType(tileName).length + 1).takeWhile(_ != '-').toInt
  }
}
