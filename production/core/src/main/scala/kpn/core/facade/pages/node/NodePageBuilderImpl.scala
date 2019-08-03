package kpn.core.facade.pages.node

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
import kpn.shared.node.NodeChangesPage
import kpn.shared.node.NodeDetailsPage
import kpn.shared.node.NodeMapPage
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

  def buildDetailsPage(user: Option[String], nodeId: Long): Option[NodeDetailsPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeDetailsPage)
    }
    else {
      nodeRepository.nodeWithId(nodeId, Couch.uiTimeout).map { nodeInfo =>
        buildNodeDetailsPage(user, nodeInfo)
      }
    }
  }

  def buildMapPage(user: Option[String], nodeId: Long): Option[NodeMapPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeMapPage)
    }
    else {
      nodeRepository.nodeWithId(nodeId, Couch.uiTimeout).map { nodeInfo =>
        buildNodeMapPage(user, nodeInfo)
      }
    }
  }

  def buildChangesPage(user: Option[String], nodeId: Long, itemsPerPage: Int, pageIndex: Int): Option[NodeChangesPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeChangesPage)
    }
    else {
      nodeRepository.nodeWithId(nodeId, Couch.uiTimeout).map { nodeInfo =>
        buildNodeChangesPage(user, nodeInfo, itemsPerPage, pageIndex)
      }
    }
  }

  private def buildNodePage(user: Option[String], nodeInfo: NodeInfo): NodePage = {
    val nodeChangesPage = buildNodeChangesPage(user: Option[String], nodeInfo: NodeInfo, 3, 0)
    val nodeChanges = NodeChangeInfos(nodeChangesPage.changes, nodeChangesPage.incompleteWarning)
    NodePage(nodeInfo, buildNodeReferences(nodeInfo), nodeChanges)
  }

  private def buildNodeDetailsPage(user: Option[String], nodeInfo: NodeInfo): NodeDetailsPage = {
    NodeDetailsPage(nodeInfo, buildNodeReferences(nodeInfo))
  }

  private def buildNodeMapPage(user: Option[String], nodeInfo: NodeInfo): NodeMapPage = {
    NodeMapPage(nodeInfo)
  }

  private def buildNodeChangesPage(user: Option[String], nodeInfo: NodeInfo, itemsPerPage: Int, pageIndex: Int): NodeChangesPage = {
    val nodeChanges = collectNodeChanges(user, nodeInfo.id)
    val incompleteWarning = isIncomplete(nodeChanges)
    val more = nodeChanges.size > maxItemsPerPage
    val shownNodeChanges = if (more) nodeChanges.take(nodeChanges.size - 1) else nodeChanges
    val changeSetInfos = collectChangeSetInfos(shownNodeChanges, more)
    val changes = toNodeChangeInfos(shownNodeChanges, changeSetInfos)
    NodeChangesPage(nodeInfo, changes, incompleteWarning, 100)
  }

  private def buildNodeReferences(nodeInfo: NodeInfo): NodeReferences = {
    val nodeNetworkReferences = nodeRepository.nodeNetworkReferences(nodeInfo.id, Couch.uiTimeout)
    val nodeOrphanRouteReferences = nodeRepository.nodeOrphanRouteReferences(nodeInfo.id, Couch.uiTimeout)
    NodeReferences(nodeNetworkReferences, nodeOrphanRouteReferences)
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
