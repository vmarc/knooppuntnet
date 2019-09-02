// TODO migrate to Angular
package kpn.client.components.map

import kpn.client.common.map.Layers
import kpn.client.common.map.MapDefinition
import kpn.client.common.map.Util
import kpn.client.common.map.Util.toCoordinate
import kpn.client.common.map.vector.MapState
import kpn.client.common.map.vector.SelectedFeatureHolder
import kpn.shared.NetworkType
import kpn.shared.tiles.ZoomLevel

class MainMap(networkType: NetworkType, selectionHolder: SelectedFeatureHolder) extends MapDefinition {

  private val state = new MapState()

  override def onMounted(): Unit = {
    updateTarget()
    val bounds = networkType match {
      case NetworkType.horseRiding =>
        ol.Extent(
          toCoordinate(5.21, 52.03),
          toCoordinate(6.98, 52.58)
        )
      case NetworkType.motorboat =>
        ol.Extent(
          toCoordinate(4.06, 51.91),
          toCoordinate(4.59, 52.35)
        )
      case NetworkType.canoe =>
        ol.Extent(
          toCoordinate(4.06, 51.91),
          toCoordinate(4.59, 52.15)
        )
      case NetworkType.inlineSkates =>
        ol.Extent(
          toCoordinate(4.14, 51.90),
          toCoordinate(4.57, 52.00)
        )
      case _ =>
        ol.Extent(
          toCoordinate(2.24, 50.16),
          toCoordinate(10.56, 54.09)
        )
    }

    map.getView().fit(bounds)
  }

  private val vectorTileLayer = new MainMapLayer(networkType)

  private val bitmapTileLayer = Layers.bitmapTileLayer(networkType)

  override val map: ol.Map = Util.map(
    layers = Seq(Layers.osm, bitmapTileLayer, vectorTileLayer.layer /*, Layers.debug*/),
    view = ol.View(
      minZoom = ZoomLevel.minZoom,
      zoom = ZoomLevel.maxZoom
    )
  )

  vectorTileLayer.interactions(map, state, selectionHolder).foreach(map.addInteraction)

  private def zoomListener(e: ol.interaction.select.Event /*TODO MAP change event type, and verify if boolean return type makes sense at all*/): Boolean = {

    val zoom = map.getView().getZoom().toInt

    if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
      // TODO MAP change text in ui side bar --> need to zoom in to interact with map
      bitmapTileLayer.setVisible(true)
      vectorTileLayer.layer.setVisible(false)
    }
    else if (zoom >= ZoomLevel.vectorTileMinZoom) {
      bitmapTileLayer.setVisible(false)
      vectorTileLayer.layer.setVisible(true)
    }

    true
  }

  map.getView().on("change:resolution", zoomListener _)

}
