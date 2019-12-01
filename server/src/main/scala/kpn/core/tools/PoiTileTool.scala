package kpn.core.tools

import kpn.api.common.tiles.ZoomLevel
import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.tiles.PoiTilesBuilder
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.vector.PoiVectorTileBuilder
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.NodeTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl

/*
  Generates tiles for all points of interest in the database.

  Example use from sbt:
    runMain kpn.core.tools.PoiTileTool -t /kpn/tiles -d pois
 */
object PoiTileTool {

  val log = Log(classOf[PoiTileTool])

  def main(args: Array[String]): Unit = {

    val exit = PoiTileToolOptions.parse(args) match {
      case Some(options) =>

        Couch.executeIn(options.poiDatabaseName) { database =>

          val poiRepository = new PoiRepositoryImpl(database)
          val tileFileRepository = new TileFileRepositoryImpl(options.tileDir, "mvt")
          val tileCalculator = new TileCalculatorImpl()
          val nodeTileAnalyzer = new NodeTileAnalyzerImpl(tileCalculator)

          val tileTool = new PoiTileTool(
            poiRepository,
            new PoiTilesBuilder(
              new PoiVectorTileBuilder(),
              tileFileRepository,
              nodeTileAnalyzer
            )
          )

          tileTool.make()
        }

        log.info("Done")
        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }

}

class PoiTileTool(
  poiRepository: PoiRepository,
  poiTilesBuilder: PoiTilesBuilder
) {

  def make(): Unit = {

    val pois = poiRepository.allPois()

    PoiTileTool.log.info(s"Processing ${pois.size} pois")

    (ZoomLevel.poiTileMinZoom to ZoomLevel.poiTileMaxZoom).foreach { z =>
      poiTilesBuilder.build(z, pois)
    }
  }

}
