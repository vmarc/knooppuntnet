package kpn.server.api.analysis.pages.node

import kpn.api.common.RouteType
import kpn.api.common.node.MapNodeDetail
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class MapNodeDetailBuilderImpl(nodeRepository: NodeRepository) extends MapNodeDetailBuilder {

  override def build(routeType: RouteType, nodeId: Long): Option[MapNodeDetail] = {
    nodeRepository.nodeWithId(nodeId).map { nodeDoc =>
      MapNodeDetail(
        nodeDoc._id,
        nodeDoc.routeTypeName(routeType),
        nodeDoc.latitude,
        nodeDoc.longitude,
        nodeDoc.lastUpdated,
        nodeDoc.networkReferences,
        nodeDoc.routeReferences
      )
    }
  }
}
