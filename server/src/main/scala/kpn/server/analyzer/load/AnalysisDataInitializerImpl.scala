package kpn.server.analyzer.load

import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class AnalysisDataInitializerImpl(
  analysisContext: AnalysisContext,
  networkRepository: NetworkRepository,
  routeRepository: RouteRepository,
  nodeRepository: NodeRepository
) extends AnalysisDataInitializer {

  private val log = Log(classOf[AnalysisDataInitializerImpl])

  override def load(): Unit = {
    Log.context("load-watched") {
      log.infoElapsed {
        val networkCount = loadWatchedNetworks()
        val routeCount = loadWatchedRoutes()
        val nodeCount = loadWatchedNodes()
        (s"completed: $networkCount networks, $routeCount routes, $nodeCount nodes", ())
      }
    }
  }

  private def loadWatchedNetworks(): Int = {
    log.infoElapsed {
      networkRepository.activeNetworkIds().foreach { networkId =>
        analysisContext.data.networks.watched.add(networkId, ElementIds())
      }
      val networkCount = analysisContext.data.networks.watched.size
      (s"$networkCount networks", networkCount)
    }
  }

  private def loadWatchedRoutes(): Int = {
    log.infoElapsed {
      routeRepository.activeRouteElementIds().foreach { routeElementIds =>
        analysisContext.data.routes.watched.add(routeElementIds._id, routeElementIds.elementIds)
      }
      val routeCount = analysisContext.data.routes.watched.size
      (s"$routeCount routes", routeCount)
    }
  }

  private def loadWatchedNodes(): Int = {
    log.infoElapsed {
      nodeRepository.activeNodeIds().foreach { nodeId =>
        analysisContext.data.nodes.watched.add(nodeId)
      }
      val nodeCount = analysisContext.data.nodes.watched.size
      (s"$nodeCount counts", nodeCount)
    }
  }

}
