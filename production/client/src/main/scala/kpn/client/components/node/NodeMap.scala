// Migrated to Angular
package kpn.client.components.node

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
import kpn.shared.NodeInfo
import kpn.shared.tiles.ZoomLevel

class NodeMap(nodeInfo: NodeInfo)(implicit context: Context) extends MapDefinition {

  private val nodeCoordinate = toCoordinate(nodeInfo)

  private val rwnVectorTileLayer = if (nodeInfo.rwnName.isEmpty) {
    None
  } else {
    val layer = new MainMapLayer(NetworkType.hiking)
    layer.layer.set("name", nls("Hiking", "Wandelen"))
    Some(layer)
  }

  private val rcnVectorTileLayer = if (nodeInfo.rcnName.isEmpty) {
    None
  } else {
    val layer = new MainMapLayer(NetworkType.bicycle)
    layer.layer.set("name", nls("Bicycle", "Fietsen"))
    Some(layer)
  }

  private val rhnVectorTileLayer = if (nodeInfo.rhnName.isEmpty) {
    None
  } else {
    val layer = new MainMapLayer(NetworkType.horseRiding)
    layer.layer.set("name", nls("Horse riding", "Ruiter"))
    Some(layer)
  }

  private val rmnVectorTileLayer = if (nodeInfo.rmnName.isEmpty) {
    None
  } else {
    val layer = new MainMapLayer(NetworkType.motorboat)
    layer.layer.set("name", nls("Motorboat", "Motorboot"))
    Some(layer)
  }

  private val rpnVectorTileLayer = if (nodeInfo.rpnName.isEmpty) {
    None
  } else {
    val layer = new MainMapLayer(NetworkType.canoe)
    layer.layer.set("name", nls("Canoe", "Kano"))
    Some(layer)
  }

  private val rinVectorTileLayer = if (nodeInfo.rinName.isEmpty) {
    None
  } else {
    val layer = new MainMapLayer(NetworkType.inlineSkates)
    layer.layer.set("name", "Inline skates")
    Some(layer)
  }

  override def targetElementId: String = "node-map"

  override val layers: Seq[ol.layer.Base] = Seq(
    Some(Layers.osm),
    rwnVectorTileLayer.map(_.layer),
    rcnVectorTileLayer.map(_.layer),
    rhnVectorTileLayer.map(_.layer),
    rmnVectorTileLayer.map(_.layer),
    rpnVectorTileLayer.map(_.layer),
    rinVectorTileLayer.map(_.layer),
    Some(markerLayer())
  ).flatten

  override val map: ol.Map = Util.map(
    layers = layers,
    view = ol.View(
      center = nodeCoordinate,
      minZoom = ZoomLevel.vectorTileMinZoom,
      maxZoom = ZoomLevel.vectorTileMaxOverZoom,
      zoom = ZoomLevel.vectorTileMaxZoom
    )
  )

  rwnVectorTileLayer.foreach(_.interactions(map, new MapState(), new SelectedFeatureHolder()))
  rcnVectorTileLayer.foreach(_.interactions(map, new MapState(), new SelectedFeatureHolder()))
  rhnVectorTileLayer.foreach(_.interactions(map, new MapState(), new SelectedFeatureHolder()))
  rmnVectorTileLayer.foreach(_.interactions(map, new MapState(), new SelectedFeatureHolder()))
  rpnVectorTileLayer.foreach(_.interactions(map, new MapState(), new SelectedFeatureHolder()))
  rinVectorTileLayer.foreach(_.interactions(map, new MapState(), new SelectedFeatureHolder()))

  val networkTypeCount = Seq(
    rwnVectorTileLayer,
    rwnVectorTileLayer,
    rcnVectorTileLayer,
    rhnVectorTileLayer,
    rmnVectorTileLayer,
    rpnVectorTileLayer,
    rinVectorTileLayer
  ).flatten.size

  if (networkTypeCount > 1) {
    if (rwnVectorTileLayer.isDefined) {
      rwnVectorTileLayer.get.layer.setVisible(false)
    }
    if (rcnVectorTileLayer.isDefined) {
      rcnVectorTileLayer.get.layer.setVisible(false)
    }
    if (rhnVectorTileLayer.isDefined) {
      rhnVectorTileLayer.get.layer.setVisible(false)
    }
    if (rmnVectorTileLayer.isDefined) {
      rmnVectorTileLayer.get.layer.setVisible(false)
    }
    if (rpnVectorTileLayer.isDefined) {
      rpnVectorTileLayer.get.layer.setVisible(false)
    }
    if (rinVectorTileLayer.isDefined) {
      rinVectorTileLayer.get.layer.setVisible(false)
    }
  }

  override def onMounted(): Unit = {
    updateTarget()
  }

  private def markerLayer(): ol.layer.Layer = {
    val source = ol.source.Vector()
    source.addFeature(Marker.marker(nodeCoordinate, "blue"))
    val layer = ol.layer.Vector(source)
    layer.set("name", nls("Node", "Knooppunt"))
    layer
  }

}
