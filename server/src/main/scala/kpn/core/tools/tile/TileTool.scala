package kpn.core.tools.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType
import kpn.core.tools.tile.TileTool.log
import kpn.core.util.Log
import kpn.core.util.Memory
import kpn.core.util.Redesign
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.tile.TileFileBuilderImpl
import kpn.server.analyzer.engine.tiles.TileDataLoader
import kpn.server.analyzer.engine.tiles.TileDataLoaderImpl
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.TilesBuilder
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy
import scala.concurrent.ExecutionContext

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
            Redesign.tileGenerationNetworkTypes.foreach(networkType => tileTool.make(networkType, false))
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
      new TileDataLoaderImpl(
        nodeRepository,
        routeRepository,
        tileDataNodeBuilder
      )
    }
    val executor = buildExecutor()
    val executionContext: ExecutionContext = ExecutionContext.fromExecutor(executor)
    val tilesBuilder: TilesBuilder = {
      val bitmapTileFileRepository = new TileFileRepositoryImpl(tileDir, "png")
      val vectorTileFileRepository = new TileFileRepositoryImpl(tileDir, "mvt")
      val tileFileBuilder = new TileFileBuilderImpl(bitmapTileFileRepository, vectorTileFileRepository)
      new TilesBuilder(
        bitmapTileFileRepository,
        vectorTileFileRepository,
        tileFileBuilder
      )(executionContext)
    }

    new TileTool(
      tileAnalyzer,
      tilesBuilder
    )
  }

  private def buildExecutor(): ThreadPoolTaskExecutor = {
    val executor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(6)
    executor.setMaxPoolSize(6)
    executor.setRejectedExecutionHandler(new CallerRunsPolicy)
    executor.setThreadNamePrefix("tile-builder-")
    executor.initialize()
    executor
  }
}

class TileTool(
  tileAnalyzer: TileDataLoader,
  tilesBuilder: TilesBuilder
) {

  def make(networkType: NetworkType, nodeNetwork: Boolean): Unit = {
    Log.context(networkType.name) {
      log.info("Start tile analysis")
      val memoryBefore = Memory.bytes
      val tileData = tileAnalyzer.load(networkType, nodeNetwork)
      val memoryAfter = Memory.bytes
      log.info(s"Memory allocated for tile analysis: ${(memoryAfter - memoryBefore) / 1024 / 1024}M")
      (ZoomLevel.minZoom to ZoomLevel.vectorTileMaxZoom).foreach { z =>
        Log.context(s"$z") {
          log.infoElapsed {
            tilesBuilder.build(z, tileData)
            (s"tile building done", ())
          }
        }
      }
    }
  }
}
