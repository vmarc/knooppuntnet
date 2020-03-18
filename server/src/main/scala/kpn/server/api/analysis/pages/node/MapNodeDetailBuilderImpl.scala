package kpn.server.api.analysis.pages.node

import kpn.api.common.common.Ref
import kpn.api.common.node.MapNodeDetail
import kpn.api.common.node.NodeNetworkReference
import kpn.api.common.node.NodeNetworkRouteReference
import kpn.api.common.node.NodeOrphanRouteReference
import kpn.api.custom.NetworkType
import kpn.core.db.couch.Couch
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class MapNodeDetailBuilderImpl(
  nodeRepository: NodeRepository
) extends MapNodeDetailBuilder {

  def build(user: Option[String], networkType: NetworkType, nodeId: Long): Option[MapNodeDetail] = {

    nodeRepository.nodeWithId(nodeId, Couch.uiTimeout).map { nodeInfo =>

      val networkReferences = buildNetworkReferences(networkType, nodeInfo.id)
      val routeReferences = buildRouteReferences(networkReferences, networkType, nodeInfo.id)

      MapNodeDetail(
        nodeInfo.id,
        nodeInfo.name(networkType),
        nodeInfo.latitude,
        nodeInfo.longitude,
        nodeInfo.lastUpdated,
        networkReferences.map(toRef),
        routeReferences
      )
    }
  }

  private def buildNetworkReferences(networkType: NetworkType, nodeId: Long): Seq[NodeNetworkReference] = {
    nodeRepository.nodeNetworkReferences(nodeId, Couch.uiTimeout)
      .filter(_.networkType == networkType)
      .filter(_.nodeConnection == false)
  }

  private def buildRouteReferences(networkRefs: Seq[NodeNetworkReference], networkType: NetworkType, nodeId: Long): Seq[Ref] = {
    val networkRouteRefs = networkRefs.flatMap(r => r.routes).map(toRef)
    val orphanRouteRefs = retrieveOrphanRouteRefs(networkType, nodeId)
    (networkRouteRefs ++ orphanRouteRefs).distinct.sortBy(_.name)
  }

  private def retrieveOrphanRouteRefs(networkType: NetworkType, nodeId: Long): Seq[Ref] = {
    nodeRepository.nodeOrphanRouteReferences(nodeId, Couch.uiTimeout)
      .filter(_.networkType == networkType)
      .map(toRef)
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
