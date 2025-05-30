package kpn.server.analyzer.engine.tile

import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.OldTileData
import kpn.server.analyzer.engine.tiles.TileBuilder
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

  private val log = Log(classOf[TileFileBuilderImpl])

  def build(tileData: OldTileData, tile: Tile): Unit = {
    //    if (tile.z <= ZoomLevel.bitmapTileMaxZoom) {
    //      buildRasterStandard(tileData, tile)
    //      buildRasterSurface(tileData, tile)
    //      buildRasterSurvey(tileData, tile)
    //      buildRasterAnalysis(tileData, tile)
    //      if (tile.z == ZoomLevel.vectorTileMinZoom - 1) { // TODO redesign tiles - both mvt and png at zoom level 11 ???
    //        buildVector(tileData, tile)
    //      }
    //    }
    //    else {
    buildVector(tileData, tile)
    //    }
  }

  private def buildVector(tileData: OldTileData, tile: Tile): Unit = {
    if (tileData.isEmpty) {
      vectorTileRepository.deleteTile(tileData.routeType.entryName, tile)
    }
    else {
      val tileBytes = new VectorTileBuilder().build(tileData, tile)
      if (tileBytes.nonEmpty) {
        vectorTileRepository.saveOrUpdate(tileData.routeType.entryName, tile, tileBytes)
      }
      else {
        log.info("empty tile")
      }
    }
  }

  private def buildRasterStandard(tileData: OldTileData, tile: Tile): Unit = {
    build(new RasterTileBuilder(new TileColorSurface), tileData.routeType.entryName, tileData, tile)
  }

  private def buildRasterSurface(tileData: OldTileData, tile: Tile): Unit = {
    build(new RasterTileBuilder(new TileColorSurface), s"${tileData.routeType.entryName}/surface", tileData, tile)
  }

  private def buildRasterSurvey(tileData: OldTileData, tile: Tile): Unit = {
    build(new RasterTileBuilder(new TileColorSurvey(SurveyDateInfoBuilder.dateInfo)), s"${tileData.routeType.entryName}/survey", tileData, tile)
  }

  private def buildRasterAnalysis(tileData: OldTileData, tile: Tile): Unit = {
    build(new RasterTileBuilder(new TileColorAnalysis), s"${tileData.routeType.entryName}/analysis", tileData, tile)
  }

  private def build(tileBuilder: TileBuilder, tileType: String, tileData: OldTileData, tile: Tile): Unit = {
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
