package kpn.server.analyzer.engine.tiles

import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository

class TileDataLoaderImpl(
  nodeRepository: NodeRepository,
  routeRepository: RouteRepository,
  tileDataNodeBuilder: TileDataNodeBuilder
) extends TileDataLoader {

  private val log = Log(classOf[TileDataLoaderImpl])

  def load(routeType: RouteType, nodeNetwork: Boolean): OldTileData = {
    log.infoElapsed {
      val nodes = if (nodeNetwork) findNodes(routeType) else Seq.empty
      val routes = findRoutes(routeType, nodeNetwork)
      val tileAnalysis = OldTileData(routeType, nodes, routes)
      (s"Completed analysis for ${routeType.entryName}", tileAnalysis)
    }
  }

  private def findNodes(routeType: RouteType): Seq[TileDataNode] = {
    log.info("Find nodes")
    log.infoElapsed {
      val nodeTileInfos = nodeRepository.nodeTileInfoByrouteType(routeType)
      val tileDataNode = nodeTileInfos.flatMap(node => tileDataNodeBuilder.build(routeType, node))
      (s"${nodeTileInfos.size} node tile infos", tileDataNode)
    }
  }

  private def findRoutes(routeType: RouteType, nodeNetwork: Boolean): Seq[TileDataRoute] = {
    log.info("Find routes")
    log.infoElapsed {
      val routeTileInfos = routeRepository.routeTileInfosByRouteType(routeType, nodeNetwork)
      val tileDataRoutes = routeTileInfos.map(routeTileInfo => TileDataRouteBuilder.fromRouteInfo(routeTileInfo))
      (s"${routeTileInfos.size} routes", tileDataRoutes)
    }
  }
}
