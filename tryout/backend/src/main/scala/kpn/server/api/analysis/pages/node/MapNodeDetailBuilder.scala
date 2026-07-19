package kpn.server.api.analysis.pages.node

import kpn.api.common.RouteType
import kpn.api.common.node.MapNodeDetail
import kpn.server.repository.NodeRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class MapNodeDetailBuilder(nodeRepository: NodeRepository) {

  def build(routeType: RouteType, nodeId: Long): Option[MapNodeDetail] = {
    nodeRepository.nodeWithId(nodeId).map { nodeDoc =>
      MapNodeDetail(
        nodeDoc._id,
        nodeDoc.routeTypeName(routeType),
        nodeDoc.base.latitude,
        nodeDoc.base.longitude,
        nodeDoc.base.raw.timestamp,
        nodeDoc.networkRelationReferences,
        nodeDoc.routeReferences
      )
    }
  }
}
