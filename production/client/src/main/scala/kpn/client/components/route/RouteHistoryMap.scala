package kpn.client.components.route

import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.map.Layers
import kpn.client.common.map.MapDefinition
import kpn.client.common.map.Marker
import kpn.client.common.map.Util
import kpn.client.common.map.Util.toCoordinate
import kpn.shared.Bounds
import kpn.shared.data.raw.RawNode
import kpn.shared.route.GeometryDiff
import kpn.shared.route.PointSegment

import scala.scalajs.js

class RouteHistoryMap(
  target: String,
  nodes: Seq[RawNode],
  bounds: Bounds,
  diff: GeometryDiff
)(implicit context: Context) extends MapDefinition {

  override def targetElementId: String = target

  override val layers: Seq[ol.layer.Base] = {

    val blue = ol.Color(0, 0, 255, 0.8)
    val green = ol.Color(0, 255, 0, 0.8)
    val red = ol.Color(255, 0, 0, 1)

    Seq(
      Some(Layers.osm),
      segmentLayer(nls("Unchanged", "Onveranderd"), diff.common, 5, blue),
      segmentLayer(nls("Added", "Toegevoegd"), diff.after, 12, green),
      segmentLayer(nls("Deleted", "Verwijderd"), diff.before, 3, red),
      nodeLayer()
    ).flatten
  }

  override val map: ol.Map = Util.map(
    layers = layers,
    view = ol.View(
      minZoom = 1,
      maxZoom = 19
    )
  )

  override def onMounted(): Unit = {
    updateTarget()
    fitBounds()
  }

  private def nodeLayer(): Option[ol.layer.Layer] = {
    if (nodes.isEmpty) {
      None
    }
    else {
      val source = ol.source.Vector()
      nodes.foreach { node =>
        source.addFeature(Marker.marker(toCoordinate(node), "blue"))
      }
      val layer = ol.layer.Vector(source)
      layer.set("name", nls("Nodes", "Knooppunten"))
      Some(layer)
    }
  }


  private def segmentLayer(name: String, segments: Seq[PointSegment], width: Double, color: ol.Color): Option[ol.layer.Layer] = {
    if (segments.isEmpty) {
      None
    }
    else {

      val style = ol.style.Style(
        stroke = ol.style.Stroke(
          color = color,
          width = width
        )
      )

      val source = ol.source.Vector()

      segments.foreach { s =>
        val p1 = toCoordinate(s.p1)
        val p2 = toCoordinate(s.p2)

        val feature = new ol.Feature(new ol.geom.LineString(js.Array(p1, p2)))
        feature.setStyle(style)
        source.addFeature(feature)
      }

      val layer = ol.layer.Vector(source)
      layer.set("name", name)
      Some(layer)
    }
  }

  private def fitBounds(): Unit = {

    val lattitudes = (diff.before ++ diff.after).flatMap(s => Seq(s.p1.lat, s.p2.lat))
    val longitudes = (diff.before ++ diff.after).flatMap(s => Seq(s.p1.lon, s.p2.lon))

    if (lattitudes.isEmpty) {
      val southWest = toCoordinate(bounds.minLon, bounds.minLat)
      val northEast = toCoordinate(bounds.maxLon, bounds.maxLat)
      map.getView().fit(ol.Extent(southWest, northEast))
    }
    else {
      val latMin = lattitudes.min
      val lonMin = longitudes.min
      val latMax = lattitudes.max
      val lonMax = longitudes.max

      val latDelta = (latMax - latMin) / 8
      val lonDelta = (lonMax - lonMin) / 8

      val southWest = toCoordinate(lonMin - lonDelta, latMin - latDelta)
      val northEast = toCoordinate(lonMax + lonDelta, latMax + latDelta)

      map.getView().fit(ol.Extent(southWest, northEast))
    }
  }
}
