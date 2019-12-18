package kpn.core.tools.poi

import kpn.core.db.couch.Couch
import kpn.core.util.Log
import kpn.server.analyzer.engine.poi.PoiTileBuilder
import kpn.server.analyzer.engine.poi.PoiTileBuilderImpl
import kpn.server.analyzer.engine.poi.PoiTileTask
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.vector.PoiVectorTileBuilder
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl
import kpn.server.repository.TaskRepository
import kpn.server.repository.TaskRepositoryImpl

object PoiTileUpdateTool {

  def main(args: Array[String]): Unit = {

    val exit = PoiTileUpdateToolOptions.parse(args) match {
      case Some(options) =>

        Couch.executeIn(options.host, options.poiDatabaseName) { poiDatabase =>
          Couch.executeIn(options.host, options.taskDatabaseName) { taskDatabase =>

            val poiRepository = new PoiRepositoryImpl(poiDatabase)
            val taskRepository = new TaskRepositoryImpl(taskDatabase)
            val tileCalculator = new TileCalculatorImpl()
            val tileFileRepository = new TileFileRepositoryImpl(options.tileDir, "mvt")

            val tileBuilder = new PoiVectorTileBuilder()

            val tileTool = new PoiTileUpdateTool(
              poiRepository,
              taskRepository,
              new PoiTileBuilderImpl(
                poiRepository,
                tileCalculator,
                tileFileRepository,
                tileBuilder
              )
            )

            tileTool.make()
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

class PoiTileUpdateTool(
  poiRepository: PoiRepository,
  taskRepository: TaskRepository,
  poiTileBuilder: PoiTileBuilder,
) {

  private val log = Log(classOf[PoiTileUpdateTool])

  def make(): Unit = {

    val tasks = taskRepository.all(PoiTileTask.prefix)

    log.info(s"Processing ${tasks.size} poi tile tasks")

    tasks.take(50).zipWithIndex.foreach { case (task, index) =>
      if (((index + 1) % 10) == 0) {
        log.info(s"${index + 1}/${tasks.size}")
      }
      val tileName = PoiTileTask.tileName(task)
      poiTileBuilder.build(tileName)
      taskRepository.delete(task)
    }

    log.info("Done")
  }

}
