package kpn.server.api.analysis.pages.node

import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.node.MapNodeDetail
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class MapNodeDetailBuilderImpl(nodeRepository: NodeRepository) extends MapNodeDetailBuilder {

  override def build(routeType: RouteType, nodeId: Long): Option[MapNodeDetail] = {
    nodeRepository.nodeWithId(nodeId).map { nodeDoc =>
      val networkReferences = buildNetworkReferences(routeType, nodeDoc._id)
      val routeReferences = buildRouteReferences(routeType, nodeDoc._id)
      MapNodeDetail(
        nodeDoc._id,
        nodeDoc.routeTypeName(routeType),
        nodeDoc.latitude,
        nodeDoc.longitude,
        nodeDoc.lastUpdated,
        networkReferences,
        routeReferences
      )
    }
  }

  private def buildNetworkReferences(routeType: RouteType, nodeId: Long): Seq[Reference] = {
    nodeRepository.nodeNetworkReferences(nodeId)
      .filter(_.routeType == routeType)
  }

  private def buildRouteReferences(routeType: RouteType, nodeId: Long): Seq[Reference] = {
    nodeRepository.nodeRouteReferences(nodeId)
      .filter(_.routeType == routeType)
      .sortBy(_.name)
  }
}
