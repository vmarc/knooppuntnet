package kpn.core.tools.tile

import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.core.util.ThreadExecutor
import kpn.database.base.Database
import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.tile.RouteTileEncoder
import kpn.server.analyzer.engine.tile.ZoomLevel
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.TilesData
import kpn.server.repository.NodeRepository
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteRepositoryImpl

/*
  Generates tiles for all nodes and routes in the database.

  Example use:
    kpn.core.tools.tile.TileTool -t /kpn/tiles -d kpn-next
 */
object TileTool extends Tool[TileToolOptions] {
  private val log = Log(classOf[TileTool])

  override def options: Options[TileToolOptions] = TileToolOptions

  override def execute(options: TileToolOptions): Unit = {
    Mongo.executeIn(options.databaseName) { database =>
      val tool = buildTool(database, options.tileDir)
      tool.process()
    }
    log.info("Done")
  }

  private def buildTool(database: Database, tileDir: String): TileTool = {
    val nodeRepository = new NodeRepositoryImpl(database)
    val routeRepository = new RouteRepositoryImpl(database)
    val vectorTileFileRepository = new TileFileRepositoryImpl(tileDir, "mvt")
    val tileDataNodeBuilder = new TileDataNodeBuilderImpl()
    val routeTileEncoder = new RouteTileEncoder(
      vectorTileFileRepository,
      tileDataNodeBuilder
    )
    new TileTool(
      nodeRepository,
      routeRepository,
      routeTileEncoder
    )
  }
}

class TileTool(
  nodeRepository: NodeRepository,
  routeRepository: RouteRepository,
  routeTileEncoder: RouteTileEncoder
) {
  private val log = Log(classOf[TileTool])

  def process(): Unit = {
    RouteType.values.foreach { routeType =>
      ZoomLevel.minZoom to ZoomLevel.maxZoom foreach { zoomLevel =>
        Log.context(Seq(routeType.toString, zoomLevel.toString)) {
          processTiles(routeType, zoomLevel)
        }
      }
    }
  }

  private def processTiles(routeType: RouteType, zoomLevel: Int): Unit = {
    val tileDatas = collectTileData(routeType, zoomLevel)
    val tileNames = tileDatas.tileNames.toVector
    val context = Log.contextMessages
    ThreadExecutor.stringExecute(25, tileNames) { (index, count, tileName) =>
      Log.context(context) {
        Log.context(s"$index/$count $tileName") {
          val tileData = tileDatas.tileData(tileName)
          routeTileEncoder.encode(tileData)
        }
      }
    }
  }

  private def collectTileData(routeType: RouteType, zoomLevel: Int): TilesData = {
    val nodeTileInfos = nodeRepository.tileInfosByZoomLevel(routeType, zoomLevel)
    val routeTileInfos = routeRepository.tileInfosByZoomLevel(routeType, zoomLevel)

    val nodeTileInfosByTileName = nodeTileInfos.groupBy(_.tileName)
    val routeTileInfosByTileName = routeTileInfos.groupBy(_.tileName)

    TilesData(
      routeType,
      zoomLevel,
      nodeTileInfosByTileName,
      routeTileInfosByTileName
    )
  }
}
