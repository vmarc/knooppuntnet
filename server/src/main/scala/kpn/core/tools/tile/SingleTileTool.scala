package kpn.core.tools.tile

import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.tile.RouteTileEncoder
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilder
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.domain.RouteTiles
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository

object SingleTileTool {
  private val log = Log(classOf[TileTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      val tool = buildTool(database, "/Users/marc/kpn/tiles")
      tool.makeRouteTiles(3669758)
      // tool.make(RouteType.cycling, 6, 32, 21)
    }
    log.info("Done")
  }

  private def buildTool(database: Database, tileDir: String): SingleTileTool = {
    val nodeRepository = new NodeRepository(database)
    val routeRepository = new RouteRepository(database)
    val routeTileEncoder = {
      val vectorTileFileRepository = new TileFileRepository(tileDir, "mvt")
      val tileDataNodeBuilder = new TileDataNodeBuilder()
      new RouteTileEncoder(
        vectorTileFileRepository,
        tileDataNodeBuilder
      )
    }
    new SingleTileTool(
      nodeRepository,
      routeRepository,
      routeTileEncoder
    )
  }
}

class SingleTileTool(
  nodeRepository: NodeRepository,
  routeRepository: RouteRepository,
  routeTileEncoder: RouteTileEncoder
) {

  def makeRouteTiles(routeId: Long): Unit = {
    val routeTileInfos = routeRepository.routeTiles(routeId)
    routeTileInfos.foreach { routeTileInfo =>
      routeTileInfo.routeTypes.foreach { routeType =>
        make(routeType, routeTileInfo.z.toInt, routeTileInfo.x.toInt, routeTileInfo.y.toInt)
      }
    }
  }

  def make(routeType: RouteType, z: Int, x: Int, y: Int): Unit = {
    val tileData = buildTileData(routeType, z, x, y)
    routeTileEncoder.encode(tileData)
  }

  private def buildTileData(routeType: RouteType, z: Int, x: Int, y: Int) = {
    val tileId = TileId(z, x, y)
    val nodeTileInfos = nodeRepository.tileInfosByTileId(routeType, tileId)
    val routeTileInfos = routeRepository.tileInfosByTileId(routeType, tileId)
    val tile = RouteTiles.tile(tileId)
    val tileData = TileData(
      routeType,
      tile,
      nodeTileInfos,
      routeTileInfos
    )
    tileData
  }
}
