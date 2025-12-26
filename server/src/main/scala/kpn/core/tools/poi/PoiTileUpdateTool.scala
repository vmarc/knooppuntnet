package kpn.core.tools.poi

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.poi.PoiTileBuilder
import kpn.server.analyzer.engine.poi.PoiTileUpdater
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.vector.PoiVectorTileBuilder
import kpn.server.repository.PoiRepositoryImpl
import kpn.server.repository.TaskRepositoryImpl

object PoiTileUpdateTool extends Tool[PoiTileUpdateToolOptions] {
  private val log = Log(classOf[PoiTileUpdateTool])

  override def options: Options[PoiTileUpdateToolOptions] = PoiTileUpdateToolOptions

  override def execute(options: PoiTileUpdateToolOptions): Unit = {
    Mongo.executeIn(options.poiDatabaseName) { database =>
      val tool = buildTool(options, database)
      tool.update()
    }
  }

  private def buildTool(options: PoiTileUpdateToolOptions, database: Database): PoiTileUpdateTool = {
    val poiTileBuilder = {
      val tileBuilder = new PoiVectorTileBuilder()
      val poiRepository = new PoiRepositoryImpl(database)
      val tileFileRepository = new TileFileRepository(options.tileDir, "mvt")
      new PoiTileBuilder(
        poiRepository,
        tileFileRepository,
        tileBuilder
      )
    }
    val taskRepository = new TaskRepositoryImpl(database)
    val poiTileUpdater = new PoiTileUpdater(
      poiTileBuilder,
      taskRepository
    )
    new PoiTileUpdateTool(poiTileUpdater)
  }
}

class PoiTileUpdateTool(poiTileUpdaterImpl: PoiTileUpdater) {

  private val log = Log(classOf[PoiTileUpdateTool])

  def update(): Unit = {
    poiTileUpdaterImpl.update()
    log.info("Done")
  }
}
