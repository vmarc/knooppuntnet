// Migrated to Angular: subset-map.component.ts
package kpn.client.components.subset.networks

import kpn.client.common.Context
import kpn.client.common.map.Layers
import kpn.client.common.map.MapDefinition
import kpn.client.common.map.Marker
import kpn.client.common.map.Util
import kpn.client.common.map.Util.toCoordinate
import kpn.shared.common.Ref
import kpn.shared.network.NetworkAttributes

class SubsetNetworksMap(networkAttributess: Seq[NetworkAttributes], networkClicked: Ref => Unit)(implicit context: Context) extends MapDefinition {

  override val layers = Seq(Layers.osm, markerLayer())

  override val map: ol.Map = Util.map(
    layers = layers,
    view = ol.View(
      minZoom = 7
    )
  )

  override def onMounted(): Unit = {
    updateTarget()
    fitBounds()
  }

  private val moveInteraction: ol.interaction.Select = {
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

  private val clickInteraction: ol.interaction.Select = {
    val select = new ol.interaction.Select(
      olx.interaction.SelectOptions(
        condition = ol.events.condition.Condition.singleClick _,
        multi = false,
        style = ol.style.Style() // this overrides the normal openlayers default edit style
      )
    )
    select.on("select", scalaSingleClickListener _)
    select
  }

  private def scalaPointerMoveListener(e: ol.interaction.select.Event): Boolean = {
    if (e.selected.length > 0) {
      map.getTargetElement().setAttribute("style", "cursor: pointer")
    }
    else {
      map.getTargetElement().setAttribute("style", "cursor: default")
    }
    true
  }

  private def scalaSingleClickListener(e: ol.interaction.select.Event): Boolean = {
    if (e.selected.length > 0) {
      val feature = e.selected.head.asInstanceOf[ol.render.Feature]
      val networkId = feature.get("networkId").toString.toLong
      val networkName = feature.get("networkName").toString
      networkClicked(Ref(networkId, networkName))
    }
    true
  }

  map.addInteraction(moveInteraction)
  map.addInteraction(clickInteraction)

  private def markerLayer(): ol.layer.Layer = {
    val source = ol.source.Vector()
    networkAttributess.foreach { networkAttributes =>
      networkAttributes.center.foreach { center =>
        val centerCoordinate = toCoordinate(center.lon, center.lat)
        val feature = Marker.marker(centerCoordinate, "blue")
        feature.set("networkId", networkAttributes.id.toString)
        feature.set("networkName", networkAttributes.name)
        source.addFeature(feature)
      }
    }
    ol.layer.Vector(source)
  }

  private def fitBounds(): Unit = {

    val centers = networkAttributess.flatMap(_.center)
    val minLat = centers.map(_.lat).min
    val maxLat = centers.map(_.lat).max
    val minLon = centers.map(_.lon).min
    val maxLon = centers.map(_.lon).max

    val southWest = toCoordinate(minLon, minLat)
    val northEast = toCoordinate(maxLon, maxLat)

    val extent = ol.Extent(southWest, northEast)

    map.getView().fit(extent)
  }
}
