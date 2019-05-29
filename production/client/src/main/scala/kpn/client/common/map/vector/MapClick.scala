// TODO migrate to Angular
package kpn.client.common.map.vector

class MapClick(state: MapState, selectionHolder: SelectedFeatureHolder, refreshCallback: () => Unit) {

  val interaction: ol.interaction.Select = {
    val select = new ol.interaction.Select(
      olx.interaction.SelectOptions(
        condition = ol.events.condition.Condition.click _,
        multi = false,
        style = ol.style.Style() // this overrides the normal openlayers default edit style
      )
    )
    select.on("select", selectListener _)
    select
  }

  private def selectListener(e: ol.interaction.select.Event): Boolean = {

    e.deselected.foreach { feature: ol.Feature =>
      val layer = feature.asInstanceOf[ol.render.Feature].get("layer").toString
      if (layer.endsWith("route")) {
        state.selectedRouteId = None
      }
      else if (layer.endsWith("node")) {
        state.selectedNodeId = None
      }
    }

    if (e.selected.isEmpty) {
      selectionHolder.select(None)
    }
    else {
      e.selected.foreach { feature: ol.Feature =>
        val layer = feature.asInstanceOf[ol.render.Feature].get("layer").toString
        if (layer.endsWith("route")) {
          val id = feature.asInstanceOf[ol.render.Feature].get("id").toString
          val id2 = id.takeWhile(_ != '-')
          val name = feature.asInstanceOf[ol.render.Feature].get("name").toString
          state.selectedRouteId = Some(id2 + "-")
          val selection = SelectedRoute(id2.toLong, name)
          selectionHolder.select(Some(selection))
        }
        else if (layer.endsWith("node")) {
          state.selectedRouteId = None
          val id = feature.asInstanceOf[ol.render.Feature].get("id").toString
          state.selectedNodeId = Some(id.toString)
          val name = feature.asInstanceOf[ol.render.Feature].get("name").toString
          val selection = SelectedNode(id.toLong, name)
          selectionHolder.select(Some(selection))
        }
      }
    }

    refreshCallback()

    true
  }

}
