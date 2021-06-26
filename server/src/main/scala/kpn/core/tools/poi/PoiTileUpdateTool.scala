package kpn.core.tools.poi

import kpn.core.db.couch.Couch
import kpn.core.util.Log
import kpn.server.analyzer.engine.poi.PoiTileBuilderImpl
import kpn.server.analyzer.engine.poi.PoiTileUpdaterImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.vector.PoiVectorTileBuilder
import kpn.server.repository.PoiRepositoryImpl
import kpn.server.repository.TaskRepositoryImpl

object PoiTileUpdateTool {

  def main(args: Array[String]): Unit = {

    val exit = PoiTileUpdateToolOptions.parse(args) match {
      case Some(options) =>

        Couch.executeIn(options.host, options.poiDatabaseName) { poiDatabase =>
          Couch.executeIn(options.host, options.taskDatabaseName) { taskDatabase =>

            val tool = {
              val poiTileBuilder = {
                val tileBuilder = new PoiVectorTileBuilder()
                val poiRepository = new PoiRepositoryImpl(null, poiDatabase, false)
                val tileCalculator = new TileCalculatorImpl()
                val tileFileRepository = new TileFileRepositoryImpl(options.tileDir, "mvt")
                new PoiTileBuilderImpl(
                  poiRepository,
                  tileCalculator,
                  tileFileRepository,
                  tileBuilder
                )
              }
              val taskRepository = new TaskRepositoryImpl(null, taskDatabase, false)
              val poiTileUpdater = new PoiTileUpdaterImpl(
                poiTileBuilder,
                taskRepository
              )
              new PoiTileUpdateTool(poiTileUpdater)
            }
            tool.update()
          }
        }

        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }

}

class PoiTileUpdateTool(poiTileUpdaterImpl: PoiTileUpdaterImpl) {

  private val log = Log(classOf[PoiTileUpdateTool])

  def update(): Unit = {
    poiTileUpdaterImpl.update()
    log.info("Done")
  }

}
