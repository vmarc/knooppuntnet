package kpn.client.common.map

import scala.scalajs.js

object Marker {

  def marker(nodeCoordinate: ol.Coordinate, color: String): ol.Feature = {
    val marker = new ol.Feature(ol.geom.Point(nodeCoordinate))
    marker.setStyle(Marker.style(color))
    marker
  }

  private def style(color: String): ol.style.Style = {
    ol.style.Style(
      image = ol.style.Icon(
        anchor = js.Array[Double](12, 41),
        src = "/assets/images/marker-icon-" + color + ".png"
      )
    )
  }
}
