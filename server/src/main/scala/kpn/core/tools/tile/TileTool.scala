package kpn.core.tools.tile

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType
import kpn.core.db.couch.Couch
import kpn.core.mail.Mail
import kpn.core.mail.MailConfigReader
import kpn.core.mail.MailImpl
import kpn.core.util.Log
import kpn.server.analyzer.engine.DatabaseIndexer
import kpn.server.analyzer.engine.tile.NodeTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tiles.TileAnalyzer
import kpn.server.analyzer.engine.tiles.TileAnalyzerImpl
import kpn.server.analyzer.engine.tiles.TileBuilder
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.TilesBuilder
import kpn.server.analyzer.engine.tiles.raster.RasterTileBuilder
import kpn.server.analyzer.engine.tiles.vector.VectorTileBuilder
import kpn.server.repository.NetworkRepositoryImpl
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

    val mail: Mail = {
      val config = new MailConfigReader().read()
      new MailImpl(config)
    }

    val exit: Int = try {
      TileToolOptions.parse(args) match {
        case Some(options) =>

          def createTilesBuilder(tileBuilder: TileBuilder, extension: String): TilesBuilder = {
            val tileCalculator = new TileCalculatorImpl()
            val nodeTileAnalyzer = new NodeTileAnalyzerImpl(tileCalculator)
            val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
            val tileFileRepository = new TileFileRepositoryImpl(options.tileDir, extension)
            new TilesBuilder(tileBuilder, tileFileRepository, nodeTileAnalyzer, routeTileAnalyzer)
          }

          Couch.executeIn(options.analysisDatabaseName) { analysisDatabase =>

            new DatabaseIndexer(analysisDatabase, null, null, null, null).index()

            val tileAnalyzer = {
              val networkRepository = new NetworkRepositoryImpl(analysisDatabase)
              val orphanRepository = new OrphanRepositoryImpl(analysisDatabase)
              val routeRepository = new RouteRepositoryImpl(analysisDatabase, null)
              new TileAnalyzerImpl(
                networkRepository,
                orphanRepository,
                routeRepository
              )
            }

            val tileTool = new TileTool(
              tileAnalyzer,
              createTilesBuilder(new VectorTileBuilder(), "mvt"),
              createTilesBuilder(new RasterTileBuilder(), "png")
            )

            NetworkType.all.foreach(tileTool.make)
          }

          log.info("Done")
          mail.send("TileTool done", "all ok")

          0

        case None =>
          // arguments are bad, error message will have been displayed
          mail.send("TileTool alarm!", "Did not start")
          -1
      }
    }
    catch {
      case e: Throwable =>
        mail.send("TileTool failed!", e.getMessage)
        log.error(e.getMessage)
        -1
    }

    System.exit(exit)
  }

}

class TileTool(
  tileAnalyzer: TileAnalyzer,
  vectorTilesBuilder: TilesBuilder,
  bitmapTilesBuilder: TilesBuilder
) {

  def make(networkType: NetworkType): Unit = {

    val tileAnalysis = tileAnalyzer.analysis(networkType)

    (ZoomLevel.bitmapTileMinZoom to ZoomLevel.bitmapTileMaxZoom).foreach { z =>
      bitmapTilesBuilder.build(z, tileAnalysis)
    }

    (ZoomLevel.vectorTileMinZoom to ZoomLevel.vectorTileMaxZoom).foreach { z =>
      vectorTilesBuilder.build(z, tileAnalysis)
    }
  }

}
