package kpn.server.analyzer.engine.tile

import kpn.api.common.RouteType
import kpn.api.common.tiles.ZoomLevel
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileContext
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class TileUpdaterImpl(
  taskRepository: TaskRepository,
  nodeRepository: NodeRepository,
  routeRepository: RouteRepository,
  tileCalculator: TileCalculator,
  routeTileEncoder: RouteTileEncoder
) extends TileUpdater {

  private val log = Log(classOf[TileUpdaterImpl])

  override def update(): Unit = {
    val allTileTasks = taskRepository.all(TileTask.prefix)
    processAllTileTasks(allTileTasks)
  }

  private def processAllTileTasks(allTasks: Seq[String]): Unit = {
    log.debug(s"processing ${allTasks.size} tile tasks")
    (ZoomLevel.minZoom to ZoomLevel.maxZoom).foreach { zoomLevel =>
      val tasks = allTasks.filter(task => TileTask.zoomLevel(task) == zoomLevel)
      processZoomLevelTileTasks(zoomLevel, tasks)
    }
  }

  private def processZoomLevelTileTasks(zoomLevel: Int, tasks: Seq[String]): Unit = {
    log.debug(s"processing ${tasks.size} tasks at zoomLevel $zoomLevel")
    tasks.foreach { task =>
      log.debug(s"processing task $task")
      processTask(task)
      taskRepository.delete(task)
    }
  }

  private def processTask(task: String): Unit = {
    val tile = tileCalculator.tileNamed(TileTask.tileName(task))
    val routeType = TileTask.routeType(task)
    updateTile(routeType, tile)
  }

  private def updateTile(routeType: RouteType, tile: Tile): Unit = {
    val nodeTileInfos = collectTileDataNodes(routeType, tile)
    val routeTileInfos = collectTileDataRoutes(routeType, tile)
    val tileData = TileData(routeType, tile, nodeTileInfos, routeTileInfos)
    val tileContext = TileContext.route(tile.z)
    routeTileEncoder.encode(tileContext, tileData)
  }

  private def collectTileDataNodes(routeType: RouteType, tile: Tile): Seq[NodeTileInfo] = {
    nodeRepository.tileInfosByTileId(routeType, tile.id)
  }

  private def collectTileDataRoutes(routeType: RouteType, tile: Tile): Seq[RouteTileInfo] = {
    routeRepository.tileInfosByTileId(routeType, tile.id)
  }
}
