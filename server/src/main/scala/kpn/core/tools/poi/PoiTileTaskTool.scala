package kpn.core.tools.poi

import kpn.core.util.Log
import kpn.database.base.Exit
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.poi.PoiTileTask
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl
import kpn.server.repository.TaskRepository
import kpn.server.repository.TaskRepositoryImpl

object PoiTileTaskTool {
  private val log = Log(classOf[PoiTileTaskTool])

  def main(args: Array[String]): Unit = {
    val exitCode = execute(args)
    System.exit(exitCode)
  }

  private def execute(args: Array[String]): Int = {
    try {
      PoiTileTaskToolOptions.parse(args) match {
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

  private def executeWithOptions(options: PoiTileTaskToolOptions): Int = {
    Mongo.executeIn(options.poiDatabaseName) { database =>
      val poiRepository = new PoiRepositoryImpl(database)
      val taskRepository = new TaskRepositoryImpl(database)
      val tool = new PoiTileTaskTool(poiRepository, taskRepository)
      tool.generateTasks()
    }
    Exit.Success
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
    val tilesSize = tiles.size
    log.info(s"Generating $tilesSize tile tasks")
    tiles.zipWithIndex.foreach { case (tileName, index) =>
      if (((index + 1) % 100) == 0) {
        log.info(s"${index + 1}/$tilesSize")
      }
      taskRepository.add(PoiTileTask.withTileName(tileName))
    }
    log.info("Done")
  }
}
