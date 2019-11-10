package kpn.core.tools

import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.NetworkType
import kpn.core.db.couch.Couch
import kpn.core.mail.Mail
import kpn.core.mail.MailConfigReader
import kpn.core.mail.MailImpl
import kpn.core.tiles.TileAnalyzer
import kpn.core.tiles.TileAnalyzerImpl
import kpn.core.tiles.TileBuilder
import kpn.core.tiles.TileRepositoryImpl
import kpn.core.tiles.TilesBuilder
import kpn.core.tiles.raster.RasterTileBuilder
import kpn.core.tiles.vector.VectorTileBuilder
import kpn.core.util.Log
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.OrphanRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

/*
  Generates tiles for all nodes and routes in the database.

  Example use from sbt:
    runMain kpn.core.tools.TileTool -t /kpn/tiles -a master
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
            val tileRepository = new TileRepositoryImpl(options.tileDir, extension)
            new TilesBuilder(tileBuilder, tileRepository)
          }

          Couch.executeIn(options.analysisDatabaseName) { database =>

            val tileAnalyzer = {
              val networkRepository = new NetworkRepositoryImpl(database)
              val orphanRepository = new OrphanRepositoryImpl(database)
              val routeRepository = new RouteRepositoryImpl(database)
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
