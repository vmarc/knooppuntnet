package kpn.server.api.analysis.pages.location

import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesPage
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.LocationNodesType
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
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

    val allNodeCount = locationRepository.nodeCount(locationKey, LocationNodesType.all)
    val factsNodeCount = locationRepository.nodeCount(locationKey, LocationNodesType.facts)
    val surveyNodeCount = locationRepository.nodeCount(locationKey, LocationNodesType.survey)

    val nodeCount = parameters.locationNodesType match {
      case LocationNodesType.all => allNodeCount
      case LocationNodesType.facts => factsNodeCount
      case LocationNodesType.survey => surveyNodeCount
      case _ => 0
    }

    Some(
      LocationNodesPage(
        TimeInfoBuilder.timeInfo,
        summary,
        nodeCount,
        allNodeCount,
        factsNodeCount,
        surveyNodeCount,
        enrichedNodes
      )
    )
  }

  private def withRouteReferences(networkType: NetworkType, nodes: Seq[LocationNodeInfo]): Seq[LocationNodeInfo] = {
    val nodeIds = nodes.map(_.id)
    val nodesRouteReferences = {
      ScopedNetworkType.withNetworkType(networkType).flatMap { scopedNetworkType =>
        nodeRouteRepository.nodesRouteReferences(scopedNetworkType, nodeIds)
      }
    }
    val nodeRouteReferencesMap = nodesRouteReferences.map(nrr => nrr.nodeId -> nrr.routeRefs).toMap
    nodes.map { node =>
      nodeRouteReferencesMap.get(node.id) match {
        case Some(routeRefs) => node.copy(routeReferences = routeRefs)
        case None => node
      }
    }
  }
}
