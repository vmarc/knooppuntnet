package kpn.core.tiles.vector.encoder

import com.google.protobuf.nano.MessageNano
import kpn.core.tiles.vector.ProtobufVectorTile

/*
  Turns the tile layer information into a byte array, with the help of ProtobufVectorTile.
*/
class VectorTileEncoderEncoder(layers: VectorTileLayers, extent: Int) {

  def encode: Array[Byte] = {
    val tile = new ProtobufVectorTile.Tile
    tile.layers = buildLayers()
    MessageNano.toByteArray(tile)
  }

  private def buildLayers(): Array[ProtobufVectorTile.Tile.Layer] = {
    layers.layerNames.map { layerName =>
      val layer = layers.layerWithName(layerName)
      buildLayer(layerName, layer)
    }.toArray
  }

  private def buildLayer(layerName: String, layer: VectorTileLayer): ProtobufVectorTile.Tile.Layer = {
    val tileLayer = new ProtobufVectorTile.Tile.Layer
    tileLayer.setExtent(extent)
    tileLayer.version = 2
    tileLayer.name = layerName
    tileLayer.keys = layer.keys.toArray
    tileLayer.values = buildLayerValues(layer).toArray
    tileLayer.features = buildFeatures(layer).toArray
    tileLayer
  }

  private def buildLayerValues(layer: VectorTileLayer): Seq[ProtobufVectorTile.Tile.Value] = {
    layer.values.map { value =>
      val tileValue = new ProtobufVectorTile.Tile.Value
      tileValue.setStringValue(value)
      tileValue
    }
  }

  private def buildFeatures(layer: VectorTileLayer): Seq[ProtobufVectorTile.Tile.Feature] = {
    layer.features.map { vectorTileFeature =>
      val feature = new ProtobufVectorTile.Tile.Feature
      feature.setType(GeometryTypeEncoder.encode(vectorTileFeature.geometry))
      feature.geometry = new CommandEncoder().makeCommands1(vectorTileFeature.geometry).toArray
      feature.tags = vectorTileFeature.tags.toArray
      feature
    }
  }

}
