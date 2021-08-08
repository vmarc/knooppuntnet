package kpn.server.api.analysis.pages.node

import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.node.MapNodeDetail
import kpn.api.common.node.NodeNetworkReference
import kpn.api.common.node.NodeNetworkRouteReference
import kpn.api.common.node.NodeOrphanRouteReference
import kpn.api.custom.NetworkType
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class MapNodeDetailBuilderImpl(
  nodeRepository: NodeRepository
) extends MapNodeDetailBuilder {

  def build(user: Option[String], networkType: NetworkType, nodeId: Long): Option[MapNodeDetail] = {

    nodeRepository.nodeWithId(nodeId).map { nodeDoc =>

      val networkReferences = buildNetworkReferences(networkType, nodeDoc._id)
      val routeReferences = buildRouteReferences(networkReferences, networkType, nodeDoc._id)

      MapNodeDetail(
        nodeDoc._id,
        nodeDoc.networkTypeName(networkType),
        nodeDoc.latitude,
        nodeDoc.longitude,
        nodeDoc.lastUpdated,
        networkReferences,
        routeReferences
      )
    }
  }

  private def buildNetworkReferences(networkType: NetworkType, nodeId: Long): Seq[Reference] = {
    nodeRepository.nodeNetworkReferences(nodeId)
      .filter(_.networkType == networkType)
  }

  private def buildRouteReferences(networkRefs: Seq[Reference], networkType: NetworkType, nodeId: Long): Seq[Reference] = {
    nodeRepository.nodeRouteReferences(nodeId)
      .filter(_.networkType == networkType)
      .sortBy(_.name)
  }

  private def toRef(ref: NodeNetworkReference): Ref = {
    Ref(ref.networkId, ref.networkName)
  }

  private def toRef(ref: NodeOrphanRouteReference): Ref = {
    Ref(ref.routeId, ref.routeName)
  }

  private def toRef(ref: NodeNetworkRouteReference): Ref = {
    Ref(ref.routeId, ref.routeName)
  }
}
