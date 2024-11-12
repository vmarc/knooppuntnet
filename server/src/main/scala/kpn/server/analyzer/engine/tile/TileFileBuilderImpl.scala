package kpn.server.analyzer.engine.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.tiles.TileBuilder
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.raster.RasterTileBuilder
import kpn.server.analyzer.engine.tiles.raster.TileColorAnalysis
import kpn.server.analyzer.engine.tiles.raster.TileColorSurface
import kpn.server.analyzer.engine.tiles.raster.TileColorSurvey
import kpn.server.analyzer.engine.tiles.vector.VectorTileBuilder
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import org.springframework.stereotype.Component

@Component
class TileFileBuilderImpl(
  rasterTileRepository: TileFileRepository,
  vectorTileRepository: TileFileRepository
) extends TileFileBuilder {

  def build(tileData: TileData, tile: Tile): Unit = {
    if (tile.z <= ZoomLevel.bitmapTileMaxZoom) {
      buildRasterStandard(tileData, tile)
      buildRasterSurface(tileData, tile)
      buildRasterSurvey(tileData, tile)
      buildRasterAnalysis(tileData, tile)
      if (tile.z == ZoomLevel.vectorTileMinZoom - 1) { // TODO redesign tiles - both mvt and png at zoom level 11 ???
        buildVector(tileData, tile)
      }
    }
    else {
      buildVector(tileData, tile)
    }
  }

  private def buildVector(tileData: TileData, tile: Tile): Unit = {
    if (tileData.isEmpty) {
      vectorTileRepository.deleteTile(tileData.networkType.name, tile)
    }
    else {
      val tileBytes = new VectorTileBuilder().build(tileData, tile)
      if (tileBytes.nonEmpty) {
        vectorTileRepository.saveOrUpdate(tileData.networkType.name, tile, tileBytes)
      }
    }
  }

  private def buildRasterStandard(tileData: TileData, tile: Tile): Unit = {
    build(new RasterTileBuilder(new TileColorSurface), tileData.networkType.name, tileData, tile)
  }

  private def buildRasterSurface(tileData: TileData, tile: Tile): Unit = {
    build(new RasterTileBuilder(new TileColorSurface), s"${tileData.networkType.name}/surface", tileData, tile)
  }

  private def buildRasterSurvey(tileData: TileData, tile: Tile): Unit = {
    build(new RasterTileBuilder(new TileColorSurvey(SurveyDateInfoBuilder.dateInfo)), s"${tileData.networkType.name}/survey", tileData, tile)
  }

  private def buildRasterAnalysis(tileData: TileData, tile: Tile): Unit = {
    build(new RasterTileBuilder(new TileColorAnalysis), s"${tileData.networkType.name}/analysis", tileData, tile)
  }

  private def build(tileBuilder: TileBuilder, tileType: String, tileData: TileData, tile: Tile): Unit = {
    if (tileData.isEmpty) {
      rasterTileRepository.deleteTile(tileType, tile)
    }
    else {
      val tileBytes = tileBuilder.build(tileData, tile)
      if (tileBytes.length > 0) {
        rasterTileRepository.saveOrUpdate(tileType, tile, tileBytes)
      }
    }
  }
}
