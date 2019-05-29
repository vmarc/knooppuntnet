// TODO migrate to Angular
package kpn.client.components.map

import kpn.client.common.map.Layers
import kpn.client.common.map.vector.MapClick
import kpn.client.common.map.vector.MapMove
import kpn.client.common.map.vector.MapState
import kpn.client.common.map.vector.SelectedFeatureHolder
import kpn.shared.NetworkType

class MainMapLayer(networkType: NetworkType) {

  val layer: ol.layer.VectorTile = Layers.vectorTileLayer(networkType)

  def interactions(map: ol.Map, state: MapState, selectionHolder: SelectedFeatureHolder): Seq[ol.interaction.Select] = {

    val mainStyle = new MainStyle(map, state)
    layer.setStyle(mainStyle.jsStyleFunction)

    def refreshCallback(): Unit = {
      layer.setStyle(mainStyle.jsStyleFunction)
    }

    Seq(
      new MapClick(state, selectionHolder, refreshCallback).interaction,
      new MapMove(state, map, refreshCallback).interaction
    )
  }
}
