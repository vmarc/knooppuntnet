package kpn.core.tools.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType
import kpn.core.db.couch.Couch
import kpn.core.util.Log
import kpn.server.analyzer.engine.DatabaseIndexer
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
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

          Couch.executeIn(options.analysisDatabaseHost, options.analysisDatabaseName) { analysisDatabase =>

            new DatabaseIndexer(analysisDatabase, null, null, null, null, false).index(true)

            val nodeAnalyzer = new NodeAnalyzerImpl()
            val tileDataNodeBuilder = new TileDataNodeBuilderImpl(nodeAnalyzer)
            val tileAnalyzer = {
              val networkRepository = new NetworkRepositoryImpl(null, analysisDatabase, false)
              val orphanRepository = new OrphanRepositoryImpl(null, analysisDatabase, false)
              val nodeRepository = new NodeRepositoryImpl(null, analysisDatabase, false)
              val routeRepository = new RouteRepositoryImpl(null, analysisDatabase, false)
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
