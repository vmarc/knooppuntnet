package kpn.core.facade.pages.node

import kpn.core.db.couch.Couch
import kpn.core.engine.changes.builder.NodeChangeInfoBuilder
import kpn.core.repository.ChangeSetInfoRepository
import kpn.core.repository.ChangeSetRepository
import kpn.core.repository.NodeRepository
import kpn.shared.NodeInfo
import kpn.shared.Timestamp
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.filter.ChangesParameters
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

  def buildChangesPage(user: Option[String], nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeChangesPage)
    }
    else {
      nodeRepository.nodeWithId(nodeId, Couch.uiTimeout).map { nodeInfo =>
        buildNodeChangesPage(user, nodeInfo, parameters)
      }
    }
  }

  private def buildNodePage(user: Option[String], nodeInfo: NodeInfo): NodePage = {
    val parameters = ChangesParameters().copy(nodeId = Some(nodeInfo.id))
    val nodeChangesPage = buildNodeChangesPage(user: Option[String], nodeInfo: NodeInfo, parameters)
    val nodeChanges = NodeChangeInfos(nodeChangesPage.changes, nodeChangesPage.incompleteWarning)
    NodePage(nodeInfo, buildNodeReferences(nodeInfo), nodeChanges)
  }

  private def buildNodeDetailsPage(user: Option[String], nodeInfo: NodeInfo): NodeDetailsPage = {
    val changeCount = changeSetRepository.nodeChangesCount(nodeInfo.id)
    NodeDetailsPage(nodeInfo, buildNodeReferences(nodeInfo), changeCount)
  }

  private def buildNodeMapPage(user: Option[String], nodeInfo: NodeInfo): NodeMapPage = {
    val changeCount = changeSetRepository.nodeChangesCount(nodeInfo.id)
    NodeMapPage(nodeInfo, changeCount)
  }

  private def buildNodeChangesPage(user: Option[String], nodeInfo: NodeInfo, parameters: ChangesParameters): NodeChangesPage = {
    val changesFilter = changeSetRepository.nodeChangesFilter(nodeInfo.id, parameters.year, parameters.month, parameters.day)
    val totalCount = changesFilter.currentItemCount(parameters.impact)
    val nodeChanges = if (user.isDefined) {
      changeSetRepository.nodeChanges(parameters)
    }
    else {
      // user is not logged in; we do not show change information
      Seq()
    }

    val incompleteWarning = isIncomplete(nodeChanges)
    val changeSetIds = nodeChanges.map(_.key.changeSetId)
    val changeSetInfos = changeSetInfoRepository.all(changeSetIds)
    val changes = nodeChanges.map { nodeChange =>
      new NodeChangeInfoBuilder().build(nodeChange, changeSetInfos)
    }
    NodeChangesPage(nodeInfo, changesFilter, changes, incompleteWarning, totalCount)
  }

  private def buildNodeReferences(nodeInfo: NodeInfo): NodeReferences = {
    val nodeNetworkReferences = nodeRepository.nodeNetworkReferences(nodeInfo.id, Couch.uiTimeout)
    val nodeOrphanRouteReferences = nodeRepository.nodeOrphanRouteReferences(nodeInfo.id, Couch.uiTimeout)
    NodeReferences(nodeNetworkReferences, nodeOrphanRouteReferences)
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

}
