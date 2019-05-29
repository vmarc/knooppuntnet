// TODO migrate to Angular
package kpn.client.components.network.map

import kpn.client.common.map.Layers
import kpn.client.common.map.vector.MapClick
import kpn.client.common.map.vector.MapMove
import kpn.client.common.map.vector.SelectedFeatureHolder
import kpn.client.components.map.MainStyle
import kpn.shared.NetworkType

class NetworkMapLayer(networkType: NetworkType) {

  val layer: ol.layer.VectorTile = Layers.vectorTileLayer(networkType)

  def interactions(map: ol.Map, state: NetworkMapState, selectionHolder: SelectedFeatureHolder): Seq[ol.interaction.Select] = {

    val mainStyle = new NetworkMapStyle(map, state)
    layer.setStyle(mainStyle.jsStyleFunction)

    def refreshCallback(): Unit = {
      layer.setStyle(mainStyle.jsStyleFunction)
    }

    Seq(
      new MapClick(state.state, selectionHolder, refreshCallback).interaction,
      new MapMove(state.state, map, refreshCallback).interaction
    )
  }
}
