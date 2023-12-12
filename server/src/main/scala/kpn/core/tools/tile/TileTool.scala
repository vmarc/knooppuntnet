package kpn.core.tools.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.core.tools.tile.TileTool.log
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.LinesTileCalculatorImpl
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileFileBuilderImpl
import kpn.server.analyzer.engine.tiles.TileAnalyzer
import kpn.server.analyzer.engine.tiles.TileAnalyzerImpl
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.TilesBuilder
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

/*
  Generates tiles for all nodes and routes in the database.

  Example use:
    kpn.core.tools.tile.TileTool -t /kpn/tiles -d kpn-test
 */
object TileTool {

  private val log = Log(classOf[TileTool])

  def main(args: Array[String]): Unit = {

    val exit: Int = try {
      TileToolOptions.parse(args) match {
        case Some(options) =>

          Mongo.executeIn(options.databaseName) { database =>
            val tileTool = buildTileTool(database, options.tileDir)
            NetworkType.all.foreach(tileTool.make)
          }

          log.info("Done")

          0

        case None =>
          // arguments are bad, error message will have been displayed
          -1
      }
    }
    catch {
      case e: Throwable =>
        log.error(e.getMessage)
        -1
    }

    System.exit(exit)
  }

  private def buildTileTool(database: Database, tileDir: String): TileTool = {

    val tileDataNodeBuilder = new TileDataNodeBuilderImpl()

    val tileAnalyzer = {
      val nodeRepository = new NodeRepositoryImpl(database)
      val routeRepository = new RouteRepositoryImpl(database)
      new TileAnalyzerImpl(
        nodeRepository,
        routeRepository,
        tileDataNodeBuilder
      )
    }

    val tilesBuilder: TilesBuilder = {
      val tileCalculator = new TileCalculatorImpl()
      val bitmapTileFileRepository = new TileFileRepositoryImpl(tileDir, "png")
      val vectorTileFileRepository = new TileFileRepositoryImpl(tileDir, "mvt")
      val tileFileBuilder = new TileFileBuilderImpl(bitmapTileFileRepository, vectorTileFileRepository)
      val nodeTileCalculator = new NodeTileCalculatorImpl(tileCalculator)
      val linesTileCalculator = new LinesTileCalculatorImpl(tileCalculator)
      val routeTileCalculator = new RouteTileCalculatorImpl(linesTileCalculator)
      new TilesBuilder(
        bitmapTileFileRepository,
        vectorTileFileRepository,
        tileFileBuilder,
        nodeTileCalculator,
        routeTileCalculator
      )
    }

    new TileTool(
      tileAnalyzer,
      tilesBuilder
    )
  }
}

class TileTool(
  tileAnalyzer: TileAnalyzer,
  tilesBuilder: TilesBuilder
) {

  def make(networkType: NetworkType): Unit = {
    Log.context(networkType.name) {
      log.info("Start tile analysis")
      val tileAnalysis = tileAnalyzer.analysis(networkType)
      (ZoomLevel.minZoom to ZoomLevel.vectorTileMaxZoom).foreach { z =>
        Log.context(s"$z") {
          tilesBuilder.build(z, tileAnalysis)
        }
      }
    }
  }
}
