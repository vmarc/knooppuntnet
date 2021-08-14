package kpn.server.analyzer.load

import kpn.core.util.Log
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
        analysisContext.watched.networks.add(networkId)
      }
      val networkCount = analysisContext.watched.networks.size
      (s"$networkCount networks", networkCount)
    }
  }

  private def loadWatchedRoutes(): Int = {
    log.infoElapsed {
      routeRepository.activeRouteElementIds().foreach { routeElementIds =>
        analysisContext.watched.routes.add(routeElementIds._id, routeElementIds.elementIds)
      }
      val routeCount = analysisContext.watched.routes.size
      (s"$routeCount routes", routeCount)
    }
  }

  private def loadWatchedNodes(): Int = {
    log.infoElapsed {
      nodeRepository.activeNodeIds().foreach { nodeId =>
        analysisContext.watched.nodes.add(nodeId)
      }
      val nodeCount = analysisContext.watched.nodes.size
      (s"$nodeCount counts", nodeCount)
    }
  }

}
