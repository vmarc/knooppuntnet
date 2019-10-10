package kpn.core.tiles

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepository
import kpn.core.repository.OrphanRepository
import kpn.core.repository.RouteRepository
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.route.RouteInfo

class TileAnalyzerImpl(
  networkRepository: NetworkRepository,
  orphanRepository: OrphanRepository,
  routeRepository: RouteRepository
) extends TileAnalyzer {

  private val log = Log(classOf[TileAnalyzerImpl])

  def analysis(networkType: NetworkType): TileAnalysis = {

    val subsets = Subset.all.filter(_.networkType == networkType)
    val details = subsets.flatMap { subset =>
      networkRepository.networks(subset, Couch.defaultTimeout).flatMap { networkAttributes =>
        //noinspection SideEffectsInMonadicTransformation
        log.info(s"network ${networkAttributes.name}")
        networkRepository.network(networkAttributes.id, Couch.defaultTimeout).flatMap { network =>
          network.detail
        }
      }
    }

    val nodes = details.flatMap(_.nodes)

    val routeIds = details.flatMap(_.routes.map(_.id))

    val orphanRouteIds = subsets.flatMap { subset =>
      orphanRepository.orphanRoutes(subset)
    }.map(_.id)

    val orphanNodes = subsets.flatMap { subset =>
      orphanRepository.orphanNodes(subset)
    }

    val routeInfos: Seq[RouteInfo] = loadRouteAnalyses(routeRepository, routeIds ++ orphanRouteIds)

    TileAnalysis(
      networkType,
      nodes,
      orphanNodes,
      routeInfos
    )
  }

  private def loadRouteAnalyses(routeRepo: RouteRepository, routeIds: Seq[Long]) = {
    var progress: Int = 0
    routeIds.zipWithIndex.flatMap { case (routeId, index) =>
      val currentProgress = (100d * index / routeIds.size).round.toInt
      if (currentProgress != progress) {
        //noinspection SideEffectsInMonadicTransformation
        log.info(s"Load route ${index + 1}/${routeIds.size} $progress%")
        progress = currentProgress
      }
      routeRepo.routeWithId(routeId, Couch.defaultTimeout)
    }
  }

}
