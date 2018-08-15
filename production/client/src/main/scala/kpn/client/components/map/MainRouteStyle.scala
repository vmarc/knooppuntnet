package kpn.client.components.map

import kpn.client.common.map.vector.MapState

import scala.scalajs.js

object MainRouteStyle {

  private val routeSelectedStyle = {
    ol.style.Style(
      stroke = ol.style.Stroke(
        color = ol.Color(255, 255, 0),
        width = 14
      )
    )
  }

  private val routeStyle = ol.style.Style(
    stroke = ol.style.Stroke(
      color = MainStyleColors.green,
      width = 1
    )
  )

  def createRouteStyle(state: MapState, zoom: Int, feature: ol.render.Feature, layer: String, enabled: Boolean = true): js.Array[ol.style.Style] = {

    val routeColor = if (enabled) {
      layer match {
        case "route" => MainStyleColors.green
        case "orphan-route" => MainStyleColors.darkGreen
        case "incomplete-route" => MainStyleColors.red
        case "error-route" => ol.Color(255, 165, 0)
        case _ => MainStyleColors.gray
      }
    }
    else {
      MainStyleColors.gray
    }

    val featureId = feature.get("id").toString

    val selectedStyle = if (state.selectedRouteId.isDefined && featureId.nonEmpty && feature.get("id").toString.startsWith(state.selectedRouteId.get)) {
      Some(routeSelectedStyle)
    }
    else {
      None
    }

    routeStyle.getStroke().setColor(routeColor)

    if (zoom < 9) {
      routeStyle.getStroke().setWidth(1)
    }
    else if (zoom < 12) {
      routeStyle.getStroke().setWidth(2)
    }
    else {
      if (state.highlightedRouteId.isDefined && feature.get("id").toString.startsWith(state.highlightedRouteId.get)) {
        routeStyle.getStroke().setWidth(8)
      }
      else {
        routeStyle.getStroke().setWidth(4)
      }
    }

    selectedStyle match {
      case Some(ss) => js.Array(ss, routeStyle)
      case None => js.Array(routeStyle)
    }
  }

}
