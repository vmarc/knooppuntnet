package kpn.client.components.network.map

import kpn.client.components.map.MainNodeStyle
import kpn.client.components.map.MainRouteStyle

import scala.scalajs.js

class NetworkMapStyle(map: ol.Map, state: NetworkMapState) {

  private val scalaStyleFunction: (ol.render.Feature, Double) => js.Array[ol.style.Style] = {
    (feature: ol.render.Feature, resolution: Double) => {

      val zoom = map.getView().getZoom().toInt
      val layer = feature.get("layer").toString

      if (layer.contains("node")) {
        val nodeId = feature.get("id").toString
        val enabled = state.nodeIds.contains(nodeId)
        MainNodeStyle.createNodeStyle(state.state, zoom, feature, enabled)
      }
      else if (layer.contains("route")) {
        val enabled = featureIdIn(feature) match {
          case Some(routeId) => state.routeIds.contains(routeId)
          case None => false
        }
        MainRouteStyle.createRouteStyle(state.state, zoom, feature, layer, enabled)
      }
      else {
        js.Array()
      }
    }
  }

  val jsStyleFunction: ol.Ol.StyleFunction = scalaStyleFunction

  private def featureIdIn(feature: ol.render.Feature): Option[String] = {
    val featureIdString = feature.get("id").toString

    val separatorIndex = featureIdString.indexOf("-")
    if (separatorIndex > 1) {
      Some(featureIdString.take(separatorIndex))
    }
    else {
      None
    }
  }

}
