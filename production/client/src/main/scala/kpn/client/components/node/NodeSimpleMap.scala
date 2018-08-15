package kpn.client.components.node

import kpn.client.common.map.MapDefinition
import kpn.client.common.map.Marker
import kpn.client.common.map.Layers
import kpn.client.common.map.Util
import kpn.client.common.map.Util.toCoordinate
import kpn.shared.LatLon

class NodeSimpleMap(target: String, position: LatLon) extends MapDefinition {

  private val coordinate = toCoordinate(position)

  override val targetElementId: String = target

  override val map: ol.Map = Util.map(
    layers = Seq(Layers.osm, detailLayer()),
    view = ol.View(
      center = coordinate,
      minZoom = 7,
      zoom = 18
    )
  )

  override def onMounted(): Unit = {
    updateTarget()
  }

  private def detailLayer(): ol.layer.Layer = {
    val source = ol.source.Vector()
    source.addFeature(Marker.marker(coordinate, "blue"))
    ol.layer.Vector(source)
  }
}
