package kpn.core.load

import kpn.core.changes.ElementIds
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.load.orphan.node.OrphanNodesLoader
import kpn.core.load.orphan.route.OrphanRoutesLoader
import kpn.core.repository.FactRepository
import kpn.core.repository.OrphanRepository
import kpn.core.util.Log
import kpn.shared.Timestamp

/**
  * Loads the state of all networks at a given timestamp. Loads the current state if no timestamp is given.
  */
class AnalysisDataLoader(
  analysisData: AnalysisData,
  networksLoader: NetworksLoader,
  orphanRoutesLoader: OrphanRoutesLoader,
  orphanNodesLoader: OrphanNodesLoader,
  orphanRepository: OrphanRepository,
  factRepository: FactRepository
) {

  private val log = Log(classOf[AnalysisDataLoader])

  def load(timestamp: Timestamp): Unit = {
    Log.context(timestamp.yyyymmddhhmmss) {
      log.info("Start loading complete analysis state")
      log.elapsed {
        loadIgnoredRoutes()
        loadIgnoredNodes()
        loadIgnoredNetworkCollections()
        networksLoader.load(timestamp)
        orphanRoutesLoader.load(timestamp)
        orphanNodesLoader.load(timestamp)
        (s"Loaded current state (${analysisData.summary})", ())
      }
    }
  }

  private def loadIgnoredRoutes(): Unit = {
    log.elapsed {
      val routeIds = orphanRepository.allIgnoredRouteIds()
      routeIds.foreach { routeId =>
        analysisData.orphanRoutes.ignored.add(routeId, ElementIds())
      }
      (s"Loaded ${routeIds.size} ignored orphan route ids", ())
    }
  }

  private def loadIgnoredNodes(): Unit = {
    log.elapsed {
      val nodeIds = orphanRepository.allIgnoredNodeIds()
      nodeIds.foreach { nodeId =>
        analysisData.orphanNodes.ignored.add(nodeId)
      }
      (s"Loaded ${nodeIds.size} ignored orphan node ids", ())
    }
  }

  private def loadIgnoredNetworkCollections(): Unit = {
    log.elapsed {
      val networkCollectionIds = factRepository.networkCollections()
      networkCollectionIds.foreach { networkId =>
        analysisData.networkCollections.add(networkId)
      }
      (s"Loaded ${networkCollectionIds.size} ignored network collection ids", ())
    }
  }
}
