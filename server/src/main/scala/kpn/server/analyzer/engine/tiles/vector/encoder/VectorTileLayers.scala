package kpn.server.analyzer.engine.tiles.vector.encoder

import scala.collection.immutable.ListMap

class VectorTileLayers {

  private var layers = ListMap[String, VectorTileLayer]()

  def layerNames: Seq[String] = layers.keys.toSeq

  def layerWithName(layerName: String): VectorTileLayer = {
    layers.get(layerName) match {
      case Some(layer) => layer
      case None =>
        val layer = new VectorTileLayer()
        layers = layers + (layerName -> layer)
        layer
    }
  }

}
