package kpn.core.tools.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.NodeTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileFileBuilder
import kpn.server.analyzer.engine.tile.TileFileBuilderImpl
import kpn.server.analyzer.engine.tiles.TileAnalyzer
import kpn.server.analyzer.engine.tiles.TileAnalyzerImpl
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.TilesBuilder
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.OrphanRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

/*
  Generates tiles for all nodes and routes in the database.

  Example use:
    kpn.core.tools.tile.TileTool -t /kpn/tiles -a master
 */
object TileTool {

  private val log = Log(classOf[TileTool])

  def main(args: Array[String]): Unit = {

    val exit: Int = try {
      TileToolOptions.parse(args) match {
        case Some(options) =>

          Mongo.executeIn(options.analysisDatabaseName) { database =>

            val tileDataNodeBuilder = new TileDataNodeBuilderImpl()
            val tileAnalyzer = {
              val networkRepository = new NetworkRepositoryImpl(database)
              val orphanRepository = new OrphanRepositoryImpl(database)
              val nodeRepository = new NodeRepositoryImpl(database)
              val routeRepository = new RouteRepositoryImpl(database)
              new TileAnalyzerImpl(
                networkRepository,
                orphanRepository,
                nodeRepository,
                routeRepository,
                tileDataNodeBuilder
              )
            }

            val tileCalculator = new TileCalculatorImpl()
            val bitmapTileFileRepository = new TileFileRepositoryImpl(options.tileDir, "png")
            val vectorTileFileRepository = new TileFileRepositoryImpl(options.tileDir, "mvt")
            val tileFileBuilder: TileFileBuilder = new TileFileBuilderImpl(bitmapTileFileRepository, vectorTileFileRepository)
            val nodeTileCalculator = new NodeTileCalculatorImpl(tileCalculator)
            val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)

            val tilesBuilder: TilesBuilder = new TilesBuilder(
              bitmapTileFileRepository,
              vectorTileFileRepository,
              tileFileBuilder,
              nodeTileCalculator,
              routeTileCalculator,
              tileDataNodeBuilder
            )

            val tileTool = new TileTool(
              tileAnalyzer,
              tilesBuilder
            )

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

}

class TileTool(
  tileAnalyzer: TileAnalyzer,
  tilesBuilder: TilesBuilder
) {

  def make(networkType: NetworkType): Unit = {
    Log.context(networkType.name) {
      val tileAnalysis = tileAnalyzer.analysis(networkType)
      (ZoomLevel.minZoom to ZoomLevel.vectorTileMaxZoom).foreach { z =>
        Log.context(s"$z") {
          tilesBuilder.build(z, tileAnalysis)
        }
      }
    }
  }
}
