package kpn.server.analyzer.engine.tiles

import kpn.api.common.route.RouteInfo
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.OrphanRepository
import kpn.server.repository.RouteRepository

class TileAnalyzerImpl(
  networkRepository: NetworkRepository,
  orphanRepository: OrphanRepository,
  nodeRepository: NodeRepository,
  routeRepository: RouteRepository
) extends TileAnalyzer {

  private val log = Log(classOf[TileAnalyzerImpl])

  def analysis(networkType: NetworkType): TileAnalysis = {

    val subsets = Subset.all.filter(_.networkType == networkType)
    val details = subsets.flatMap { subset =>
      Log.context(subset.country.domain) {
        networkRepository.networks(subset, stale = false).flatMap { networkAttributes =>
          //noinspection SideEffectsInMonadicTransformation
          log.info(s"network ${networkAttributes.name}")
          networkRepository.network(networkAttributes.id).flatMap { network =>
            network.detail
          }
        }
      }
    }

    val nodes = details.flatMap(_.nodes).map(node => new TileDataNodeBuilder().build(node))

    val routeInfos = Log.context("network-routes") {
      val routeIds = details.flatMap(_.routes.map(_.id))
      loadRouteAnalyses(routeRepository, routeIds)
    }

    val orphanRouteInfos = Log.context("orphan-routes") {
      val orphanRouteIds = subsets.flatMap(subset => orphanRepository.orphanRoutes(subset)).map(_.id)
      loadRouteAnalyses(routeRepository, orphanRouteIds)
    }
    val extraNodesInOrphanRoutes = findExtraNodesInOrphanRoutes(networkType, nodes, orphanRouteInfos)
    val orphanNodes = subsets.flatMap { subset => orphanRepository.orphanNodes(subset) }

    TileAnalysis(
      networkType,
      nodes ++ extraNodesInOrphanRoutes,
      orphanNodes,
      routeInfos ++ orphanRouteInfos
    )
  }

  private def loadRouteAnalyses(routeRepo: RouteRepository, routeIds: Seq[Long]) = {
    var progress: Int = 0
    routeIds.zipWithIndex.flatMap { case (routeId, index) =>
      Log.context(s"${index  + 1}/${routeIds.size}") {
        val currentProgress = (100d * index / routeIds.size).round.toInt
        if (currentProgress != progress) {
          //noinspection SideEffectsInMonadicTransformation
          log.info(s"Load route ${index + 1}/${routeIds.size} $progress%")
          progress = currentProgress
        }
        routeRepo.routeWithId(routeId)
      }
    }
  }

  private def findExtraNodesInOrphanRoutes(networkType: NetworkType, nodes: Seq[TileDataNode], orphanRouteInfos: Seq[RouteInfo]) = {

    val orphanRouteNodeIds: Seq[Long] = orphanRouteInfos.flatMap { route =>
      val allNodes = route.analysis.map.startNodes ++
        route.analysis.map.endNodes ++
        route.analysis.map.startTentacleNodes ++
        route.analysis.map.endTentacleNodes ++
        route.analysis.map.redundantNodes
      allNodes.map(_.id)
    }

    val extraNodesInOrphanRouteIds = orphanRouteNodeIds.toSet -- nodes.map(_.id).toSet
    val extrasNodeInOrphanRoutes = extraNodesInOrphanRouteIds.flatMap(nodeId => nodeRepository.nodeWithId(nodeId))
    extrasNodeInOrphanRoutes.map(node => new TileDataNodeBuilder().build(networkType, node))
  }

}
