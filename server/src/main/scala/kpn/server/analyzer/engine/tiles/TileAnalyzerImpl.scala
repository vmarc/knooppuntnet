package kpn.server.analyzer.engine.tiles

import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository

class TileAnalyzerImpl(
  nodeRepository: NodeRepository,
  routeRepository: RouteRepository,
  tileDataNodeBuilder: TileDataNodeBuilder
) extends TileAnalyzer {

  private val log = Log(classOf[TileAnalyzerImpl])

  def analysis(networkType: NetworkType): TileAnalysis = {
    log.infoElapsed {
      val nodes = findNodes(networkType)
      val routes = findRoutes(networkType)
      val tileAnalysis = TileAnalysis(networkType, nodes, routes)
      (s"Completed analysis for ${networkType.name}", tileAnalysis)
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

  private def findRoutes(networkType: NetworkType): Seq[RouteTileInfo] = {
    log.info("Find routes")
    log.infoElapsed {
      val routeTileInfos = routeRepository.routeTileInfosByNetworkType(networkType)
      (s"${routeTileInfos.size} route tile infos", routeTileInfos)
    }
  }
}
