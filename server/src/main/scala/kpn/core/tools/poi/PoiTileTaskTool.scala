package kpn.core.tools.poi

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.poi.PoiTileTask
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl
import kpn.server.repository.TaskRepository
import kpn.server.repository.TaskRepositoryImpl

object PoiTileTaskTool extends Tool[PoiTileTaskToolOptions] {
  private val log = Log(classOf[PoiTileTaskTool])

  override def options: Options[PoiTileTaskToolOptions] = PoiTileTaskToolOptions

  override def execute(options: PoiTileTaskToolOptions): Unit = {
    Mongo.executeIn(options.poiDatabaseName) { database =>
      val tool = buildTool(options, database)
      tool.generateTasks()
    }
  }

  private def buildTool(options: PoiTileTaskToolOptions, database: Database): PoiTileTaskTool = {
    val poiRepository = new PoiRepositoryImpl(database)
    val taskRepository = new TaskRepositoryImpl(database)
    new PoiTileTaskTool(poiRepository, taskRepository)
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
