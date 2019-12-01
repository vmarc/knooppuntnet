package kpn.server.analyzer.engine.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.raster.RasterTileBuilder
import kpn.server.analyzer.engine.tiles.vector.VectorTileBuilder
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
