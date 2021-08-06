package kpn.server.analyzer.load

import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.NetworkRepository
import kpn.server.repository.OrphanRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class AnalysisDataInitializerImpl(
  analysisContext: AnalysisContext,
  networkRepository: NetworkRepository,
  routeRepository: RouteRepository,
  orphanRepository: OrphanRepository
) extends AnalysisDataInitializer {

  private val log = Log(classOf[AnalysisDataInitializerImpl])

  override def load(): Unit = {
    loadNetworks()
    loadOrphanRoutes()
    loadOrphanNodes()
  }

  private def loadNetworks(): Unit = {
    val networkIds = networkRepository.allNetworkIds()
    networkIds.zipWithIndex.foreach { case (networkId, index) =>
      Log.context(s"${index + 1}/${networkIds.size}, $networkId") {
        log.infoElapsed {
          networkRepository.elements(networkId) match {
            case None => log.error(s"Could not load elements for network with id $networkId")
            case Some(networkElements) =>
              analysisContext.data.networks.watched.add(networkId, networkElements.elementsIds)
          }
          ("Loaded", networkId)
        }
      }
    }
  }

  private def loadOrphanRoutes(): Unit = {
    val orphanRouteIds = Subset.all.flatMap(subset => orphanRepository.orphanRouteIds(subset))
    orphanRouteIds.zipWithIndex.foreach { case (routeId, index) =>
      Log.context(s"${index + 1}/${orphanRouteIds.size}, $routeId") {
        log.infoElapsed {
          routeRepository.routeElementsWithId(routeId) match {
            case None => log.error(s"Could not load elements of route with id $routeId")
            case Some(routeElements) =>
              analysisContext.data.routes.watched.add(routeId, routeElements.elementIds)
          }
          (s"Loaded route $routeId elements", ())
        }
      }
    }
  }

  private def loadOrphanNodes(): Unit = {
    val orphanNodeIds = Subset.all.flatMap(subset => orphanRepository.orphanNodes(subset)).map(_.id)
    orphanNodeIds.foreach { nodeId =>
      analysisContext.data.orphanNodes.watched.add(nodeId)
    }
  }
}
