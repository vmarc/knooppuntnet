package kpn.client.components.subset.networks

import kpn.client.common.Context
import kpn.client.common.map.Layers
import kpn.client.common.map.MapDefinition
import kpn.client.common.map.Marker
import kpn.client.common.map.Util
import kpn.client.common.map.Util.toCoordinate
import kpn.shared.network.NetworkAttributes

class SubsetNetworksMap(networkAttributess: Seq[NetworkAttributes])(implicit context: Context) extends MapDefinition {

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

  private def markerLayer(): ol.layer.Layer = {
    val source = ol.source.Vector()
    val centers = networkAttributess.flatMap(_.center)
    centers.foreach { center =>
      val centerCoordinate = toCoordinate(center.lon, center.lat)
      source.addFeature(Marker.marker(centerCoordinate, "blue"))
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
