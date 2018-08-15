package kpn.client.common.map.vector

class MapMove(state: MapState, map: ol.Map, refreshCallback: () => Unit) {

  val interaction: ol.interaction.Select = {
    val select = new ol.interaction.Select(
      olx.interaction.SelectOptions(
        condition = ol.events.condition.Condition.pointerMove _,
        multi = false,
        style = ol.style.Style() // this overrides the normal openlayers default edit style
      )
    )
    select.on("select", scalaPointerMoveListener _)
    select
  }

  private def scalaPointerMoveListener(e: ol.interaction.select.Event): Boolean = {

    if (e.selected.length > 0) {
      map.getTargetElement().setAttribute("style", "cursor: pointer")
    }
    else {
      map.getTargetElement().setAttribute("style", "cursor: default")
    }

    e.deselected.foreach { feature: ol.Feature =>
      val layer = feature.asInstanceOf[ol.render.Feature].get("layer").toString
      if (layer.endsWith("route")) {
        state.highlightedRouteId = None
      }
      else if (layer.endsWith("node")) {
        state.highlightedNodeId = None
      }
    }

    e.selected.foreach { feature: ol.Feature =>
      val layer = feature.asInstanceOf[ol.render.Feature].get("layer").toString
      if (layer.endsWith("route")) {
        val id = feature.asInstanceOf[ol.render.Feature].get("id").toString
        val id2 = id.takeWhile(_ != '-') + "-"
        state.highlightedRouteId = Some(id2)
      }
      else if (layer.endsWith("node")) {
        state.highlightedRouteId = None
        state.highlightedNodeId = Some(feature.asInstanceOf[ol.render.Feature].get("id").toString)
      }
    }

    refreshCallback()
    true
  }

}
