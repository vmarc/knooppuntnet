package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.engine.changes.builder.NodeChangeInfoBuilder
import kpn.core.repository.ChangeSetInfoRepository
import kpn.core.repository.ChangeSetRepository
import kpn.core.repository.NodeRepository
import kpn.shared.NodeInfo
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeSetInfo
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.node.NodeChangeInfo
import kpn.shared.node.NodeChangeInfos
import kpn.shared.node.NodePage
import kpn.shared.node.NodeReferences

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
        buildNodePage(user, nodeInfo)
      }
    }
  }

  private def buildNodePage(user: Option[String], nodeInfo: NodeInfo): NodePage = {
    val nodeNetworkReferences = nodeRepository.nodeNetworkReferences(nodeInfo.id, Couch.uiTimeout)
    val nodeOrphanRouteReferences = nodeRepository.nodeOrphanRouteReferences(nodeInfo.id, Couch.uiTimeout)
    val nodeReferences = NodeReferences(nodeNetworkReferences, nodeOrphanRouteReferences)
    val nodeChanges = buildNodeChanges(user, nodeInfo)
    NodePage(nodeInfo, nodeReferences, nodeChanges)
  }

  private def buildNodeChanges(user: Option[String], nodeInfo: NodeInfo): NodeChangeInfos = {
    val nodeChanges = collectNodeChanges(user, nodeInfo.id)
    val incompleteWarning = isIncomplete(nodeChanges)
    val more = nodeChanges.size > maxItemsPerPage
    val shownNodeChanges = if (more) nodeChanges.take(nodeChanges.size - 1) else nodeChanges
    val changeSetInfos = collectChangeSetInfos(shownNodeChanges, more)
    val changes = toNodeChangeInfos(shownNodeChanges, changeSetInfos)
    NodeChangeInfos(changes, incompleteWarning, more)
  }

  private def collectNodeChanges(user: Option[String], nodeId: Long): Seq[NodeChange] = {
    if (user.isDefined) {
      changeSetRepository.nodeChanges(ChangesParameters(nodeId = Some(nodeId), itemsPerPage = maxItemsPerPage + 1))
    }
    else {
      // user is not logged in; we do not show change information
      Seq()
    }
  }

  private def collectChangeSetInfos(nodeChanges: Seq[NodeChange], more: Boolean): Seq[ChangeSetInfo] = {
    val changeSetIds = nodeChanges.map(_.key.changeSetId)
    changeSetInfoRepository.all(changeSetIds)
  }

  private def isIncomplete(nodeChanges: Seq[NodeChange]) = {
    if (nodeChanges.isEmpty) {
      false
    }
    else {
      nodeChanges.last.before match {
        case Some(lastNodeChange) => lastNodeChange.version > 1 && lastNodeChange.timestamp < Timestamp.redaction
        case None => false
      }
    }
  }

  private def toNodeChangeInfos(nodeChanges: Seq[NodeChange], changeSetInfos: Seq[ChangeSetInfo]): Seq[NodeChangeInfo] = {
    nodeChanges.map { nodeChange =>
      new NodeChangeInfoBuilder().build(nodeChange, changeSetInfos)
    }
  }
}
