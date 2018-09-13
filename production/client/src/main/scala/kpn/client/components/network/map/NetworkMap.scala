package kpn.client.components.network.map

import kpn.client.common.Context
import kpn.client.common.map.Layers
import kpn.client.common.map.MapDefinition
import kpn.client.common.map.Marker
import kpn.client.common.map.Util
import kpn.client.common.map.Util.extent
import kpn.client.common.map.Util.toCoordinate
import kpn.client.common.map.vector.SelectedFeatureHolder
import kpn.shared.NetworkType
import kpn.shared.tiles.ZoomLevel

class NetworkMap(val networkMapState: NetworkMapState = new NetworkMapState())(implicit context: Context) extends MapDefinition {

  private val markerSource = ol.source.Vector()
  private val markerLayer = ol.layer.Vector(markerSource)

  val rwnVectorTileLayer = new NetworkMapLayer(NetworkType.hiking)
  val rcnVectorTileLayer = new NetworkMapLayer(NetworkType.bicycle)
  val rhnVectorTileLayer = new NetworkMapLayer(NetworkType.horse)
  val rmnVectorTileLayer = new NetworkMapLayer(NetworkType.motorboat)
  val rpnVectorTileLayer = new NetworkMapLayer(NetworkType.canoe)
  val rinVectorTileLayer = new NetworkMapLayer(NetworkType.inlineSkates)

  val rwnBitmapTileLayer: ol.layer.Tile = Layers.bitmapTileLayer(NetworkType.hiking)
  val rcnBitmapTileLayer: ol.layer.Tile = Layers.bitmapTileLayer(NetworkType.bicycle)
  val rhnBitmapTileLayer: ol.layer.Tile = Layers.bitmapTileLayer(NetworkType.horse)
  val rmnBitmapTileLayer: ol.layer.Tile = Layers.bitmapTileLayer(NetworkType.motorboat)
  val rpnBitmapTileLayer: ol.layer.Tile = Layers.bitmapTileLayer(NetworkType.canoe)
  val rinBitmapTileLayer: ol.layer.Tile = Layers.bitmapTileLayer(NetworkType.inlineSkates)

  rwnVectorTileLayer.layer.setVisible(false)
  rcnVectorTileLayer.layer.setVisible(false)
  rhnVectorTileLayer.layer.setVisible(false)
  rmnVectorTileLayer.layer.setVisible(false)
  rpnVectorTileLayer.layer.setVisible(false)
  rinVectorTileLayer.layer.setVisible(false)

  rwnBitmapTileLayer.setVisible(false)
  rcnBitmapTileLayer.setVisible(false)
  rhnBitmapTileLayer.setVisible(false)
  rmnBitmapTileLayer.setVisible(false)
  rpnBitmapTileLayer.setVisible(false)
  rinBitmapTileLayer.setVisible(false)

  override val map: ol.Map = Util.map(
    layers = Seq(
      Layers.osm,
      rwnVectorTileLayer.layer,
      rcnVectorTileLayer.layer,
      rhnVectorTileLayer.layer,
      rmnVectorTileLayer.layer,
      rpnVectorTileLayer.layer,
      rinVectorTileLayer.layer,
      rwnBitmapTileLayer,
      rcnBitmapTileLayer,
      rhnBitmapTileLayer,
      rmnBitmapTileLayer,
      rpnBitmapTileLayer,
      rinBitmapTileLayer,
      //Layers.debug,
      markerLayer
    ),
    view = ol.View(
      minZoom = ZoomLevel.minZoom,
      zoom = ZoomLevel.maxZoom
    )
  )

  rwnVectorTileLayer.interactions(map, networkMapState, new SelectedFeatureHolder())
  rcnVectorTileLayer.interactions(map, networkMapState, new SelectedFeatureHolder())
  rhnVectorTileLayer.interactions(map, networkMapState, new SelectedFeatureHolder())
  rmnVectorTileLayer.interactions(map, networkMapState, new SelectedFeatureHolder())
  rpnVectorTileLayer.interactions(map, networkMapState, new SelectedFeatureHolder())
  rinVectorTileLayer.interactions(map, networkMapState, new SelectedFeatureHolder())

  private def zoomListener(e: ol.interaction.select.Event /*TODO MAP change event type, and verify if boolean return type makes sense at all*/): Boolean = {

    val zoom = map.getView().getZoom().toInt

    if (rwnBitmapTileLayer.getVisible() || rwnVectorTileLayer.layer.getVisible()) {
      if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
        rwnBitmapTileLayer.setVisible(true)
        rwnVectorTileLayer.layer.setVisible(false)
      }
      else if (zoom >= ZoomLevel.vectorTileMinZoom) {
        rwnBitmapTileLayer.setVisible(false)
        rwnVectorTileLayer.layer.setVisible(true)
      }
    }
    else if (rcnBitmapTileLayer.getVisible() || rcnVectorTileLayer.layer.getVisible()) {
      if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
        rcnBitmapTileLayer.setVisible(true)
        rcnVectorTileLayer.layer.setVisible(false)
      }
      else if (zoom >= ZoomLevel.vectorTileMinZoom) {
        rcnBitmapTileLayer.setVisible(false)
        rcnVectorTileLayer.layer.setVisible(true)
      }
    }
    else if (rhnBitmapTileLayer.getVisible() || rhnVectorTileLayer.layer.getVisible()) {
      if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
        rhnBitmapTileLayer.setVisible(true)
        rhnVectorTileLayer.layer.setVisible(false)
      }
      else if (zoom >= ZoomLevel.vectorTileMinZoom) {
        rhnBitmapTileLayer.setVisible(false)
        rhnVectorTileLayer.layer.setVisible(true)
      }
    }
    else if (rmnBitmapTileLayer.getVisible() || rmnVectorTileLayer.layer.getVisible()) {
      if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
        rmnBitmapTileLayer.setVisible(true)
        rmnVectorTileLayer.layer.setVisible(false)
      }
      else if (zoom >= ZoomLevel.vectorTileMinZoom) {
        rmnBitmapTileLayer.setVisible(false)
        rmnVectorTileLayer.layer.setVisible(true)
      }
    }
    else if (rpnBitmapTileLayer.getVisible() || rpnVectorTileLayer.layer.getVisible()) {
      if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
        rpnBitmapTileLayer.setVisible(true)
        rpnVectorTileLayer.layer.setVisible(false)
      }
      else if (zoom >= ZoomLevel.vectorTileMinZoom) {
        rpnBitmapTileLayer.setVisible(false)
        rpnVectorTileLayer.layer.setVisible(true)
      }
    }
    else if (rinBitmapTileLayer.getVisible() || rinVectorTileLayer.layer.getVisible()) {
      if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
        rinBitmapTileLayer.setVisible(true)
        rinVectorTileLayer.layer.setVisible(false)
      }
      else if (zoom >= ZoomLevel.vectorTileMinZoom) {
        rinBitmapTileLayer.setVisible(false)
        rinVectorTileLayer.layer.setVisible(true)
      }
    }

    true
  }

  map.getView().on("change:resolution", zoomListener _)

  def refreshNodes(): Unit = {
    markerSource.clear()
    networkMapState.nodes.foreach { nodeInfo =>
      val center = toCoordinate(nodeInfo)
      markerSource.addFeature(Marker.marker(center, "blue"))
    }
  }

  override def onMounted(): Unit = {
    updateTarget()
    if (networkMapState.nodes.nonEmpty) {
      map.getView().fit(extent(networkMapState.nodes))
    }
  }
}
