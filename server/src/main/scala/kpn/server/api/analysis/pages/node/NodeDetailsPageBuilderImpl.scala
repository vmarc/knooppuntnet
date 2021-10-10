package kpn.server.api.analysis.pages.node

import kpn.api.common.NodeInfo
import kpn.api.common.node.NodeDetailsPage
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NodeDetailsPageBuilderImpl(
  nodeRepository: NodeRepository,
  changeSetRepository: ChangeSetRepository
) extends NodeDetailsPageBuilder {

  override def build(user: Option[String], nodeId: Long): Option[NodeDetailsPage] = {
    if (nodeId == 1L) {
      Some(NodeDetailsPageExample.page)
    }
    else {
      buildPage(nodeId)
    }
  }

  private def buildPage(nodeId: Long): Option[NodeDetailsPage] = {
    nodeRepository.nodeWithId(nodeId).map { nodeDoc =>
      val changeCount = changeSetRepository.nodeChangesCount(nodeId)
      val networkReferences = nodeRepository.nodeNetworkReferences(nodeId)
      val nodeRouteReferences = nodeRepository.nodeRouteReferences(nodeId)
      val mixedNetworkScopes = (
        nodeDoc.names.map(_.networkScope) ++
          nodeRouteReferences.map(_.networkScope) ++
          networkReferences.map(_.networkScope)
        ).distinct.size > 1

      val nodeInfo = NodeInfo(
        id = nodeDoc._id,
        active = nodeDoc.isActive,
        orphan = networkReferences.isEmpty && nodeRouteReferences.isEmpty,
        country = nodeDoc.country,
        name = nodeDoc.name,
        names = nodeDoc.names,
        latitude = nodeDoc.latitude,
        longitude = nodeDoc.longitude,
        lastUpdated = nodeDoc.lastUpdated,
        lastSurvey = nodeDoc.lastSurvey,
        tags = nodeDoc.tags,
        facts = nodeDoc.facts,
        locations = nodeDoc.locations,
        tiles = nodeDoc.tiles,
        integrity = nodeDoc.integrity,
        routeReferences = Seq.empty
      )

      NodeDetailsPage(
        nodeInfo,
        mixedNetworkScopes,
        nodeRouteReferences,
        networkReferences,
        nodeDoc.integrity,
        changeCount
      )
    }
  }
}
