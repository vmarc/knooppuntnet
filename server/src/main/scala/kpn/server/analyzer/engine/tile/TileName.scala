package kpn.server.analyzer.engine.tile

import kpn.api.custom.NetworkType

object TileName {

  def networkType(tileName: String): String = {
    if (tileName.contains("horse-riding")) {
      "horse-riding"
    }
    else if (tileName.contains("inline-skating")) {
      "inline-skating"
    }
    else {
      NetworkType.withNameOption(tileName.split("-").head) match {
        case Some(networkType) => networkType.entryName
        case _ => ""
      }
    }
  }

  def tileNumber(tileName: String): String = {
    tileName.substring(networkType(tileName).length + 1).replaceAll("-", "/")
  }

  def tileZoomLevel(tileName: String): Int = {
    tileName.substring(networkType(tileName).length + 1).takeWhile(_ != '-').toInt
  }
}
