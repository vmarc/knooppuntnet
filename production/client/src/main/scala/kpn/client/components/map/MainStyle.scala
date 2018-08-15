package kpn.client.components.map

import kpn.client.common.map.vector.MapState

import scala.scalajs.js

class MainStyle(map: ol.Map, state: MapState) {

  private val scalaStyleFunction: (ol.render.Feature, Double) => js.Array[ol.style.Style] = {
    (feature: ol.render.Feature, resolution: Double) => {

      val zoom = map.getView().getZoom().toInt
      val layer = feature.get("layer").toString

      if (layer.contains("node")) {
        MainNodeStyle.createNodeStyle(state, zoom, feature)
      }
      else {
        MainRouteStyle.createRouteStyle(state, zoom, feature, layer)
      }
    }
  }

  val jsStyleFunction: ol.Ol.StyleFunction = scalaStyleFunction

}
