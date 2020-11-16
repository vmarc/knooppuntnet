package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesPage
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.LocationRepository
import kpn.server.repository.NodeRouteRepository
import org.springframework.stereotype.Component

@Component
class LocationNodesPageBuilderImpl(
  locationRepository: LocationRepository,
  nodeRouteRepository: NodeRouteRepository
) extends LocationNodesPageBuilder {

  override def build(locationKey: LocationKey, parameters: LocationNodesParameters): Option[LocationNodesPage] = {
    if (locationKey == LocationKey(NetworkType.cycling, Country.nl, "example")) {
      Some(LocationNodesPageExample.page)
    }
    else {
      buildPage(locationKey, parameters)
    }
  }

  private def buildPage(locationKey: LocationKey, parameters: LocationNodesParameters): Option[LocationNodesPage] = {

    val summary = locationRepository.summary(locationKey)
    val nodes = locationRepository.nodes(locationKey, parameters)
    val enrichedNodes = withRouteReferences(locationKey.networkType, nodes)

    Some(
      LocationNodesPage(
        TimeInfoBuilder.timeInfo,
        summary,
        enrichedNodes
      )
    )
  }

  private def withRouteReferences(networkType: NetworkType, nodes: Seq[LocationNodeInfo]): Seq[LocationNodeInfo] = {
    val nodeIds = nodes.map(_.id)
    val nodesRouteReferences = nodeRouteRepository.nodesRouteReferences(networkType, nodeIds)
    val nodeRouteReferencesMap = nodesRouteReferences.map(nrr => nrr.nodeId -> nrr.routeRefs).toMap
    nodes.map { node =>
      nodeRouteReferencesMap.get(node.id) match {
        case Some(routeRefs) => node.copy(routeReferences = routeRefs)
        case None => node
      }
    }
  }

}
