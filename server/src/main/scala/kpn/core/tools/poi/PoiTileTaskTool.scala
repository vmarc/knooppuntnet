package kpn.core.tools.poi

import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.analyzer.engine.poi.PoiTileTask
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl
import kpn.server.repository.TaskRepository
import kpn.server.repository.TaskRepositoryImpl

object PoiTileTaskTool {

  def main(args: Array[String]): Unit = {

    val exit = PoiTileTaskToolOptions.parse(args) match {
      case Some(options) =>

        Mongo.executeIn(options.poiDatabaseName) { database =>
          val poiRepository = new PoiRepositoryImpl(database)
          val taskRepository = new TaskRepositoryImpl(database)
          val tool = new PoiTileTaskTool(poiRepository, taskRepository)
          tool.generateTasks()
        }

        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }
}

class PoiTileTaskTool(
  poiRepository: PoiRepository,
  taskRepository: TaskRepository
) {

  private val log = Log(classOf[PoiTileTaskTool])

  def generateTasks(): Unit = {

    log.info("Reading tile names")
    val tiles = poiRepository.allTiles().sorted

    log.info(s"Generating ${tiles.size} tile tasks")
    tiles.zipWithIndex.foreach { case (tileName, index) =>
      if (((index + 1) % 100) == 0) {
        log.info(s"${index + 1}/${tiles.size}")
      }
      taskRepository.add(PoiTileTask.withTileName(tileName))
    }
    log.info("Done")
  }
}
