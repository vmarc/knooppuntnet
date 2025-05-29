package kpn.core.tools.poi

import kpn.core.util.Log
import kpn.database.base.Exit
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.poi.PoiTileBuilderImpl
import kpn.server.analyzer.engine.poi.PoiTileUpdaterImpl
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.vector.PoiVectorTileBuilder
import kpn.server.repository.PoiRepositoryImpl
import kpn.server.repository.TaskRepositoryImpl

object PoiTileUpdateTool {
  private val log = Log(classOf[PoiTileUpdateTool])

  def main(args: Array[String]): Unit = {
    val exitCode = execute(args)
    System.exit(exitCode)
  }

  private def execute(args: Array[String]): Int = {
    try {
      PoiTileUpdateToolOptions.parse(args) match {
        case Some(options) => executeWithOptions(options)
        case None =>
          // arguments are bad, error message will have been displayed
          Exit.Failure
      }
    } catch {
      case e: Exception =>
        log.error(e.getMessage)
        Exit.Failure
    }
  }

  private def executeWithOptions(options: PoiTileUpdateToolOptions): Int = {
    Mongo.executeIn(options.poiDatabaseName) { database =>
      val tool = {
        val poiTileBuilder = {
          val tileBuilder = new PoiVectorTileBuilder()
          val poiRepository = new PoiRepositoryImpl(database)
          val tileFileRepository = new TileFileRepositoryImpl(options.tileDir, "mvt")
          new PoiTileBuilderImpl(
            poiRepository,
            tileFileRepository,
            tileBuilder
          )
        }
        val taskRepository = new TaskRepositoryImpl(database)
        val poiTileUpdater = new PoiTileUpdaterImpl(
          poiTileBuilder,
          taskRepository
        )
        new PoiTileUpdateTool(poiTileUpdater)
      }
      tool.update()
    }
    Exit.Success
  }
}

class PoiTileUpdateTool(poiTileUpdaterImpl: PoiTileUpdaterImpl) {

  private val log = Log(classOf[PoiTileUpdateTool])

  def update(): Unit = {
    poiTileUpdaterImpl.update()
    log.info("Done")
  }
}
