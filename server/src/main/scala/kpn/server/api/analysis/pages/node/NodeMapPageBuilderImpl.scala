package kpn.server.api.analysis.pages.node

import kpn.api.common.NodeMapInfo
import kpn.api.common.node.NodeMapPage
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NodeMapPageBuilderImpl(
  nodeRepository: NodeRepository,
  changeSetRepository: ChangeSetRepository
) extends NodeMapPageBuilder {

  def build(nodeId: Long): Option[NodeMapPage] = {
    if (nodeId == 1L) {
      Some(NodeMapPageExample.page)
    }
    else {
      buildPage(nodeId)
    }
  }

  private def buildPage(nodeId: Long): Option[NodeMapPage] = {
    nodeRepository.nodeWithId(nodeId).map { nodeDoc =>
      val changeCount = changeSetRepository.nodeChangesCount(nodeId)
      NodeMapPage(
        NodeMapInfo(
          nodeDoc._id,
          nodeDoc.name,
          nodeDoc.names.map(_.networkType),
          nodeDoc.latitude,
          nodeDoc.longitude
        ),
        changeCount
      )
    }
  }
}
