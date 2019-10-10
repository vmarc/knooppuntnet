package kpn.shared.common

import kpn.shared.data.Node

object MapBounds {

  private val percentageExtra = 5

  def apply(): MapBounds = MapBounds("0", "0", "0", "0")

  def apply(nodes: Seq[Node]): MapBounds = {

    if (nodes.isEmpty) {
      MapBounds("", "", "", "")
    }
    else {
      val latitudes = nodes.map(_.latitude.toDouble)
      val longitudes = nodes.map(_.longitude.toDouble)

      val latMin = latitudes.min
      val latMax = latitudes.max
      val lonMin = longitudes.min
      val lonMax = longitudes.max

      val latExtra = (latMax - latMin) / 5
      val lonExtra = (latMax - latMin) / 5

      val latMinAdjusted = (latMin - latExtra).toString
      val latMaxAdjusted = (latMax + latExtra).toString
      val lonMinAdjusted = (lonMin - latExtra).toString
      val lonMaxAdjusted = (lonMax + latExtra).toString

      MapBounds(latMinAdjusted, latMaxAdjusted, lonMinAdjusted, lonMaxAdjusted)
    }
  }
}

case class MapBounds(latMin: String, latMax: String, lonMin: String, lonMax: String)
