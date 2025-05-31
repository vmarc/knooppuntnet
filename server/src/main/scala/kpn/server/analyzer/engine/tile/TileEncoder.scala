package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Tile
import no.ecc.vectortile.VectorTileEncoder

import scala.jdk.CollectionConverters.MapHasAsJava

object TileEncoder {

  def encode(tile: Tile, features: Seq[Feature]): Array[Byte] = {
    val encoder = new VectorTileEncoder(tile.extent, tile.clipBufferSize, false)
    features.foreach { feature =>
      encoder.addFeature(feature.layer.entryName, feature.attributes.asJava, feature.geometry)
    }
    encoder.encode
  }
}
