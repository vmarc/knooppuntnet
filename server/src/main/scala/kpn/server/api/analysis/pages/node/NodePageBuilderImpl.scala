package kpn.server.api.analysis.pages.node

import kpn.api.common.NodeMapInfo
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.node.NodeChangesPage
import kpn.api.common.node.NodeDetailsPage
import kpn.api.common.node.NodeMapPage
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NodePageBuilderImpl(
  nodeRepository: NodeRepository,
  changeSetRepository: ChangeSetRepository
) extends NodePageBuilder {

  def buildDetailsPage(user: Option[String], nodeId: Long): Option[NodeDetailsPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeDetailsPage)
    }
    else {
      doBuildDetailsPage(nodeId)
    }
  }

  def buildMapPage(user: Option[String], nodeId: Long): Option[NodeMapPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeMapPage)
    }
    else {
      doBuildMapPage(nodeId)
    }
  }

  def buildChangesPage(user: Option[String], nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeChangesPage)
    }
    else {
      doBuildChangesPage(user, nodeId, parameters)
    }
  }

  private def doBuildDetailsPage(nodeId: Long): Option[NodeDetailsPage] = {
    nodeRepository.nodeWithId(nodeId).map { nodeInfo =>
      val changeCount = changeSetRepository.nodeChangesCount(nodeId)
      val networkReferences = nodeRepository.nodeNetworkReferences(nodeId)
      val mixedNetworkScopes = (
        nodeInfo.names.map(_.networkScope) ++
          nodeInfo.routeReferences.map(_.networkScope) ++
          networkReferences.map(_.networkScope)
        ).distinct.size > 1
      NodeDetailsPage(
        nodeInfo,
        mixedNetworkScopes,
        nodeInfo.routeReferences,
        networkReferences,
        nodeInfo.integrity,
        changeCount
      )
    }
  }

  private def doBuildMapPage(nodeId: Long): Option[NodeMapPage] = {
    nodeRepository.nodeWithId(nodeId).map { nodeInfo =>
      val changeCount = changeSetRepository.nodeChangesCount(nodeId)
      NodeMapPage(
        NodeMapInfo(
          nodeInfo._id,
          nodeInfo.name,
          nodeInfo.names.map(_.networkType),
          nodeInfo.latitude,
          nodeInfo.longitude
        ),
        changeCount
      )
    }
  }

  private def doBuildChangesPage(user: Option[String], nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage] = {
    nodeRepository.nodeWithId(nodeId).map { nodeInfo =>
      if (user.isDefined) {
        val nodeChanges = changeSetRepository.nodeChanges(nodeId, parameters)
        val changesFilter = changeSetRepository.nodeChangesFilter(nodeId, parameters.year, parameters.month, parameters.day)
        val totalCount = changesFilter.currentItemCount(parameters.impact)
        val incompleteWarning = isIncomplete(nodeChanges)
        val changes = nodeChanges.map { change =>
          NodeChangeInfo(
            change.id,
            change.after.map(_.version),
            change.key,
            Tags.empty,
            change.comment,
            change.before.map(_.toMeta),
            change.after.map(_.toMeta),
            change.connectionChanges,
            change.roleConnectionChanges,
            change.definedInNetworkChanges,
            change.tagDiffs,
            change.nodeMoved,
            change.addedToRoute,
            change.removedFromRoute,
            change.addedToNetwork,
            change.removedFromNetwork,
            change.factDiffs,
            change.facts,
            change.happy,
            change.investigate
          )
        }
        NodeChangesPage(
          nodeInfo._id,
          nodeInfo.name,
          changesFilter,
          changes,
          incompleteWarning,
          totalCount,
          changesFilter.totalCount
        )
      }
      else {
        // user is not logged in; we do not show change information
        NodeChangesPage(
          nodeInfo._id,
          nodeInfo.name,
          ChangesFilter.empty,
          Seq.empty,
          incompleteWarning = false,
          0,
          0
        )
      }
    }
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
