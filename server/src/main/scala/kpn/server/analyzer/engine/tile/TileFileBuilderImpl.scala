package kpn.server.analyzer.engine.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.tiles.TileBuilder
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.TileFileRepository
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

  def build(tileData: TileData): Unit = {
    if (tileData.tile.z <= ZoomLevel.bitmapTileMaxZoom) {
      buildRasterStandard(tileData)
      buildRasterSurface(tileData)
      buildRasterSurvey(tileData)
      buildRasterAnalysis(tileData)
      if (tileData.tile.z == ZoomLevel.poiTileMinZoom) {
        buildVector(tileData)
      }
    }
    else {
      buildVector(tileData)
    }
  }

  private def buildVector(tileData: TileData): Unit = {
    if (tileData.isEmpty) {
      // TODO continue implemetation...
      // vectorTileRepository.delete()
    }
    else {
      val tileBytes = new VectorTileBuilder().build(tileData)
      if (tileBytes.length > 0) {
        vectorTileRepository.saveOrUpdate(tileData.networkType.name, tileData.tile, tileBytes)
      }
    }
  }

  private def buildRasterStandard(tileData: TileData): Unit = {
    build(new RasterTileBuilder(new TileColorSurface), tileData.networkType.name, tileData)
  }

  private def buildRasterSurface(tileData: TileData): Unit = {
    build(new RasterTileBuilder(new TileColorSurface), s"${tileData.networkType.name}/surface", tileData)
  }

  private def buildRasterSurvey(tileData: TileData): Unit = {
    build(new RasterTileBuilder(new TileColorSurvey(SurveyDateInfoBuilder.dateInfo)), s"${tileData.networkType.name}/survey", tileData)
  }

  private def buildRasterAnalysis(tileData: TileData): Unit = {
    build(new RasterTileBuilder(new TileColorAnalysis), s"${tileData.networkType.name}/analysis", tileData)
  }

  private def build(tileBuilder: TileBuilder, tileType: String, tileData: TileData): Unit = {
    val tileBytes = tileBuilder.build(tileData)
    if (tileBytes.length > 0) {
      rasterTileRepository.saveOrUpdate(tileType, tileData.tile, tileBytes)
    }
  }

}
