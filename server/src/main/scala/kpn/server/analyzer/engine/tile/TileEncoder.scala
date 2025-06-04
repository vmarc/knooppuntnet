package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.TileContext
import no.ecc.vectortile.VectorTileEncoder

import scala.jdk.CollectionConverters.MapHasAsJava

object TileEncoder {

  def encode(tileContext: TileContext, features: Seq[Feature]): Array[Byte] = {
    val encoder = new VectorTileEncoder(tileContext.extent, tileContext.clipBufferSize, false)
    features.foreach { feature =>
      encoder.addFeature(feature.layer.entryName, feature.attributes.asJava, feature.geometry)
    }
    encoder.encode
  }
}
