package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.engine.changes.builder.NodeChangeInfoBuilder
import kpn.core.repository.ChangeSetInfoRepository
import kpn.core.repository.ChangeSetRepository
import kpn.core.repository.NodeRepository
import kpn.shared.Timestamp
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.node.NodeChangeInfos
import kpn.shared.node.NodePage

class NodePageBuilderImpl(
  nodeRepository: NodeRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends NodePageBuilder {

  private val maxItemsPerPage = 3

  def build(user: Option[String], nodeId: Long): Option[NodePage] = {
    if (nodeId == 1) {
      Some(NodePageExample.page)
    }
    else {
      nodeRepository.nodeWithId(nodeId, Couch.uiTimeout).map { nodeInfo =>
        val nodeReferences = nodeRepository.nodeReferences(nodeId, Couch.uiTimeout)
        val nodeChanges: Seq[NodeChange] = if (user.isDefined) {
          changeSetRepository.nodeChanges(ChangesParameters(nodeId = Some(nodeId), itemsPerPage = maxItemsPerPage + 1))
        }
        else {
          Seq()
        }

        val more = nodeChanges.size > maxItemsPerPage

        val changeSetInfos = {
          val shownNodeChanges = if (more) nodeChanges.take(nodeChanges.size - 1) else nodeChanges
          val changeSetIds = shownNodeChanges.map(_.key.changeSetId)
          changeSetInfoRepository.all(changeSetIds)
        }

        val nodeChangeInfoCollection = nodeChanges.map { nodeChange =>
          new NodeChangeInfoBuilder().build(nodeChange, changeSetInfos)
        }

        val incompleteWarning = if (nodeChanges.isEmpty) {
          false
        }
        else {
          nodeChanges.last.before match {
            case None => false
            case Some(lastNodeChange) =>
              lastNodeChange.version > 1 &&
                lastNodeChange.timestamp < Timestamp.redaction
          }
        }

        val nodeChangeInfos = NodeChangeInfos(nodeChangeInfoCollection, incompleteWarning, more)

        NodePage(nodeInfo, nodeReferences, nodeChangeInfos)
      }
    }
  }
}
