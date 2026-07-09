package kpn.api.common.common

import kpn.api.common.data.Node

object MapBounds {

  def apply(): MapBounds = MapBounds("0", "0", "0", "0")

  def apply(nodes: Seq[Node]): MapBounds = {

    if (nodes.isEmpty) {
      MapBounds("", "", "", "")
    }
    else {
      val latitudes = nodes.map(_.lat)
      val longitudes = nodes.map(_.lon)

      val latMin = latitudes.min
      val latMax = latitudes.max
      val lonMin = longitudes.min
      val lonMax = longitudes.max

      val latExtra = (latMax - latMin) / 5
      val lonExtra = (lonMax - lonMin) / 5

      val latMinAdjusted = (latMin - latExtra).toString
      val latMaxAdjusted = (latMax + latExtra).toString
      val lonMinAdjusted = (lonMin - lonExtra).toString
      val lonMaxAdjusted = (lonMax + lonExtra).toString

      MapBounds(latMinAdjusted, latMaxAdjusted, lonMinAdjusted, lonMaxAdjusted)
    }
  }
}

case class MapBounds(latMin: String, latMax: String, lonMin: String, lonMax: String)
