package kpn.server.analyzer.engine.tiles

import kpn.api.common.NetworkType
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

  def load(networkType: NetworkType, nodeNetwork: Boolean): TileData = {
    log.infoElapsed {
      val nodes = if (nodeNetwork) findNodes(networkType) else Seq.empty
      val routes = findRoutes(networkType, nodeNetwork)
      val tileAnalysis = TileData(networkType, nodes, routes)
      (s"Completed analysis for ${networkType.entryName}", tileAnalysis)
    }
  }

  private def findNodes(networkType: NetworkType): Seq[TileDataNode] = {
    log.info("Find nodes")
    log.infoElapsed {
      val nodeTileInfos = nodeRepository.nodeTileInfoByNetworkType(networkType)
      val tileDataNode = nodeTileInfos.flatMap(node => tileDataNodeBuilder.build(networkType, node))
      (s"${nodeTileInfos.size} node tile infos", tileDataNode)
    }
  }

  private def findRoutes(networkType: NetworkType, nodeNetwork: Boolean): Seq[TileDataRoute] = {
    log.info("Find routes")
    log.infoElapsed {
      val routeTileInfos = routeRepository.routeTileInfosByNetworkType(networkType, nodeNetwork)
      val tileDataRoutes = routeTileInfos.map(routeTileInfo => TileDataRouteBuilder.fromRouteInfo(routeTileInfo))
      (s"${routeTileInfos.size} routes", tileDataRoutes)
    }
  }
}
