// TODO migrate to Angular
package kpn.client.components.node

import kpn.client.common.map.MapDefinition
import kpn.client.common.map.Marker
import kpn.client.common.map.Layers
import kpn.client.common.map.Util
import kpn.client.common.map.Util.toCoordinate
import kpn.shared.LatLon

import scala.scalajs.js

class NodeHistoryMap(target: String, position: LatLon, previousPosition: LatLon)
  extends MapDefinition {

  private val coordinate = toCoordinate(position)
  private val previousCoordinate = toCoordinate(previousPosition)

  override val targetElementId: String = target

  override val map: ol.Map = Util.map(
    layers = Seq(Layers.osm, detailLayer()),
    view = ol.View(
      minZoom = 7,
      maxZoom = 19
    )
  )

  override def onMounted(): Unit = {
    updateTarget()
    fitBounds()
  }

  private def detailLayer(): ol.layer.Layer = {
    val source = ol.source.Vector()
    source.addFeature(marker)
    source.addFeature(displacement)
    ol.layer.Vector(source)
  }

  private def marker: ol.Feature = Marker.marker(coordinate, "blue")

  private def displacement: ol.Feature = {
    val feature = new ol.Feature(new ol.geom.LineString(js.Array(previousCoordinate, coordinate)))
    feature.setStyle(
      ol.style.Style(
        stroke = ol.style.Stroke(
          color = ol.Color(255, 0, 0),
          width = 5
        )
      )
    )
    feature
  }

  private def fitBounds(): Unit = {

    val lonMin = position.lon.min(previousPosition.lon)
    val lonMax = position.lon.max(previousPosition.lon)
    val latMin = position.lat.min(previousPosition.lat)
    val latMax = position.lat.max(previousPosition.lat)

    val bounds = ol.Extent(
      toCoordinate(lonMin, latMin),
      toCoordinate(lonMax, latMax)
    )

    map.getView().fit(bounds)
  }
}
