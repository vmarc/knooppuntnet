package kpn.server.analyzer.engine.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.core.tiles.TileData
import kpn.core.tiles.TileFileRepository
import kpn.core.tiles.raster.RasterTileBuilder
import kpn.core.tiles.vector.VectorTileBuilder
import org.springframework.stereotype.Component

@Component
class TileFileBuilderImpl(
  rasterTileRepository: TileFileRepository,
  vectorTileRepository: TileFileRepository
) extends TileFileBuilder {

  def build(tileData: TileData): Unit = {
    if (tileData.tile.z <= ZoomLevel.bitmapTileMaxZoom) {
      val tileBytes = new RasterTileBuilder().build(tileData)
      if (tileBytes.length > 0) {
        rasterTileRepository.saveOrUpdate(tileData.networkType.name, tileData.tile, tileBytes)
      }
    }
    else {
      val tileBytes = new VectorTileBuilder().build(tileData)
      if (tileBytes.length > 0) {
        vectorTileRepository.saveOrUpdate(tileData.networkType.name, tileData.tile, tileBytes)
      }
    }
  }
}
