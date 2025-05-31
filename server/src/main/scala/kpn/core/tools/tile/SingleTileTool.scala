package kpn.core.tools.tile

import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.tile.RouteTileEncoder
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilderImpl
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.repository.NodeRepository
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteRepositoryImpl

object SingleTileTool {
  private val log = Log(classOf[TileTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      val tool = buildTool(database, "/Users/marc/kpn/tiles")
      tool.make(RouteType.cycling, 6, 32, 21)
    }
    log.info("Done")
  }

  private def buildTool(database: Database, tileDir: String): SingleTileTool = {
    val nodeRepository = new NodeRepositoryImpl(database)
    val routeRepository = new RouteRepositoryImpl(database)
    val routeTileEncoder = {
      val vectorTileFileRepository = new TileFileRepositoryImpl(tileDir, "mvt")
      val tileDataNodeBuilder = new TileDataNodeBuilderImpl()
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

  def make(routeType: RouteType, z: Int, x: Int, y: Int): Unit = {
    val tileData = buildTileData(routeType, z, x, y)
    routeTileEncoder.encode(tileData)
  }

  private def buildTileData(routeType: RouteType, z: Int, x: Int, y: Int) = {
    val tileId = TileId(z, x, y)
    val nodeTileInfos = nodeRepository.tileInfosByTile(routeType, tileId)
    val routeTileDocs = routeRepository.tilesWithName(routeType, tileId)
    val tile = Tile.routeTileFromId(tileId)
    val tileData = TileData(
      routeType,
      tile,
      nodeTileInfos,
      routeTileDocs
    )
    tileData
  }
}
