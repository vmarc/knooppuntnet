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

    log.debugElapsed {

      val nodes = findNodes(networkType)
      val routes = findRoutes(networkType)

      (
        s"Completed analysis for ${networkType.name}",
        TileAnalysis(
          networkType,
          nodes,
          routes
        )
      )
    }
  }

  private def findNodes(networkType: NetworkType): Seq[TileDataNode] = {
    val nodeTileInfos = nodeRepository.nodeTileInfoByNetworkType(networkType)
    nodeTileInfos.flatMap(node => tileDataNodeBuilder.build(networkType, node))
  }

  private def findRoutes(networkType: NetworkType): Seq[RouteTileInfo] = {
    routeRepository.routeTileInfosByNetworkType(networkType)
  }
}
