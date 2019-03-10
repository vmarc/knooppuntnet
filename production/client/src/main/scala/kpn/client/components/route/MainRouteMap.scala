package kpn.client.components.route

import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.map.Layers
import kpn.client.common.map.MapDefinition
import kpn.client.common.map.Marker
import kpn.client.common.map.Util
import kpn.client.common.map.Util.toCoordinate
import kpn.client.common.map.vector.MapState
import kpn.client.common.map.vector.SelectedFeatureHolder
import kpn.client.components.map.MainMapLayer
import kpn.shared.NetworkType
import kpn.shared.common.TrackPath
import kpn.shared.common.TrackPoint
import kpn.shared.common.TrackSegment
import kpn.shared.route.RouteMap
import kpn.shared.route.RouteNetworkNodeInfo
import kpn.shared.tiles.ZoomLevel

import scala.scalajs.js

class MainRouteMap(networkType: NetworkType, routeMap: RouteMap)(implicit context: Context) extends MapDefinition {

  private val vectorTileLayer = new MainMapLayer(networkType)
  vectorTileLayer.layer.set("name", nls("Other routes", "Andere routes"))

  override val layers: Seq[ol.layer.Base] = Seq(
    Some(Layers.osm),
    Some(vectorTileLayer.layer),
    Some(buildMarkerLayer()),
    forwardLayer(),
    backwardLayer(),
    startTentacles(),
    endTentacles(),
    unusedSegments()
  ).flatten

  override val map: ol.Map = Util.map(
    layers = layers,
    view = ol.View(
      minZoom = ZoomLevel.vectorTileMinZoom,
      maxZoom = ZoomLevel.vectorTileMaxOverZoom,
      zoom = ZoomLevel.vectorTileMinZoom
    )
  )

  vectorTileLayer.interactions(map, new MapState(), new SelectedFeatureHolder())

  override def onMounted(): Unit = {
    updateTarget()
    fitBounds()
  }

  private def forwardLayer(): Option[ol.layer.Base] = {
    routeMap.forwardPath.map { path =>
      val title = nls("Forward route", "Heen weg")
      val forwardSegments = ol.source.Vector()
      forwardSegments.addFeature(pathToFeature(title, ol.Color(0, 0, 255, 0.3), path))
      val layer = ol.layer.Vector(forwardSegments)
      layer.set("name", title)
      layer
    }
  }

  private def backwardLayer(): Option[ol.layer.Base] = {
    routeMap.backwardPath.map { path =>
      val title = nls("Backward route", "Terug weg")
      val backwardSegments = ol.source.Vector()
      backwardSegments.addFeature(pathToFeature(title, ol.Color(0, 0, 255, 0.3), path))
      val layer = ol.layer.Vector(backwardSegments)
      layer.set("name", title)
      layer
    }
  }

  private def startTentacles(): Option[ol.layer.Base] = {
    if (routeMap.startTentaclePaths.isEmpty) {
      None
    }
    else {
      val title = nls("Start tentacle", "Start tentakel")
      val group = ol.source.Vector()
      routeMap.startTentaclePaths.foreach { path =>
        group.addFeature(pathToFeature(title, ol.Color(0, 255, 0, 0.3), path))
      }
      val layer = ol.layer.Vector(group)
      layer.set("name", title)
      Some(layer)
    }
  }

  private def endTentacles(): Option[ol.layer.Base] = {
    if (routeMap.endTentaclePaths.isEmpty) {
      None
    }
    else {
      val title = nls("End tentacle", "Eind tentakel")
      val group = ol.source.Vector()
      routeMap.endTentaclePaths.foreach { path =>
        group.addFeature(pathToFeature(title, ol.Color(255, 255, 0, 0.3), path))
      }
      val layer = ol.layer.Vector(group)
      layer.set("name", title)
      Some(layer)
    }
  }

  private def unusedSegments(): Option[ol.layer.Base] = {
    if (routeMap.unusedSegments.isEmpty) {
      None
    }
    else {
      val title = nls("Unused", "Ongebruikt")
      val group = ol.source.Vector()
      routeMap.unusedSegments.foreach { segment =>
        segmentToFeature(title, ol.Color(255, 0, 0, 0.3), segment)
      }
      val layer = ol.layer.Vector(group)
      layer.set("name", title)
      Some(layer)
    }
  }

  private def segmentToFeature(name: String, color: ol.Color, segment: TrackSegment): ol.Feature = {
    trackPointsToFeature(name, color, segment.trackPoints)
  }

  private def pathToFeature(name: String, color: ol.Color, path: TrackPath): ol.Feature = {
    val trackPoints = path.segments.flatMap(segment => segment.trackPoints)
    trackPointsToFeature(name, color, trackPoints)
  }

  private def trackPointsToFeature(name: String, color: ol.Color, trackPoints: Seq[TrackPoint]): ol.Feature = {
    val coordinates = trackPoints.map { trackPoint =>
      toCoordinate(trackPoint)
    }
    val polygon = new ol.geom.LineString(js.Array(coordinates: _*))
    val feature = new ol.Feature(polygon)
    val lineDash = new js.Array[Int]()
    lineDash.push(25)
    lineDash.push(25)

    val style = ol.style.Style(
      stroke = ol.style.Stroke(color, 15 /*, lineDash*/)
    )
    feature.setStyle(style)
    feature
  }


  private def fitBounds(): Unit = {
    val b = routeMap.bounds
    val min = toCoordinate(b.lonMin, b.latMin)
    val max = toCoordinate(b.lonMax, b.latMax)
    val extent = ol.Extent(min, max)
    map.getView().fit(extent)
  }

  private def buildMarkerLayer(): ol.layer.Layer = {

    val markers = Seq(
      buildMarkers(routeMap.startNodes, "green", nls("Start node", "Start knooppunt")),
      buildMarkers(routeMap.endNodes, "red", nls("End node", "Eind knooppunt")),
      buildMarkers(routeMap.startTentacleNodes, "orange", nls("Start tentacle node", "Start tentakel knooppunt")),
      buildMarkers(routeMap.endTentacleNodes, "purple", nls("End tentacle node", "Eind tentakel knooppunt")),
      buildMarkers(routeMap.redundantNodes, "yellow", nls("Redundant node", "Bijkomend knooppunt"))
    ).flatten

    val source = ol.source.Vector()
    source.addFeatures(js.Array(markers: _*))
    val layer = ol.layer.Vector(source)
    layer.set("name", nls("Nodes", "Knooppunten"))
    layer
  }

  private def buildMarkers(nodes: Seq[RouteNetworkNodeInfo], color: String, nodeType: String): Seq[ol.Feature] = {
    nodes.map { node =>
      val coordinate = toCoordinate(node.lon, node.lat)
      val marker = Marker.marker(coordinate, color)
      marker
    }
  }

}
