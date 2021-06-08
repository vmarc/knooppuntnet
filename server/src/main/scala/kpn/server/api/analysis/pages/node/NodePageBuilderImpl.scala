package kpn.server.api.analysis.pages.node

import kpn.api.common.NodeInfo
import kpn.api.common.NodeMapInfo
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.node.NodeChangesPage
import kpn.api.common.node.NodeDetailsPage
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.common.node.NodeMapPage
import kpn.api.common.node.NodeReferences
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.builder.NodeChangeInfoBuilder
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.MongoNodeRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.NodeRouteRepository
import org.springframework.stereotype.Component

@Component
class NodePageBuilderImpl(
  // old
  nodeRepository: NodeRepository,
  nodeRouteRepository: NodeRouteRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository,
  // new
  mongoEnabled: Boolean,
  mongoNodeRepository: MongoNodeRepository
) extends NodePageBuilder {

  def buildDetailsPage(user: Option[String], nodeId: Long): Option[NodeDetailsPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeDetailsPage)
    }
    else {
      if (mongoEnabled) {
        mongoNodeRepository.nodeWithId(nodeId).map { nodeInfo =>
          val filteredFacts = nodeInfo.facts.filter(_ != Fact.IntegrityCheckFailed)
          val filteredNodeInfo = nodeInfo.copy(facts = filteredFacts)
          buildNodeDetailsPage(user, filteredNodeInfo)
        }
      }
      else {
        nodeRepository.nodeWithId(nodeId).map { nodeInfo =>
          val filteredFacts = nodeInfo.facts.filter(_ != Fact.IntegrityCheckFailed)
          val filteredNodeInfo = nodeInfo.copy(facts = filteredFacts)
          buildNodeDetailsPage(user, filteredNodeInfo)
        }
      }
    }
  }

  def buildMapPage(user: Option[String], nodeId: Long): Option[NodeMapPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeMapPage)
    }
    else {
      if (mongoEnabled) {
        mongoNodeRepository.nodeWithId(nodeId).map { nodeInfo =>
          buildNodeMapPage(user, nodeInfo)
        }
      }
      else {
        nodeRepository.nodeWithId(nodeId).map { nodeInfo =>
          buildNodeMapPage(user, nodeInfo)
        }
      }
    }
  }

  def buildChangesPage(user: Option[String], nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeChangesPage)
    }
    else {
      if (mongoEnabled) {
        mongoNodeRepository.nodeWithId(nodeId).map { nodeInfo =>
          buildNodeChangesPage(user, nodeInfo, parameters)
        }
      }
      else {
        nodeRepository.nodeWithId(nodeId).map { nodeInfo =>
          buildNodeChangesPage(user, nodeInfo, parameters)
        }
      }
    }
  }

  private def buildNodeDetailsPage(user: Option[String], nodeInfo: NodeInfo): NodeDetailsPage = {
    val changeCount = if (mongoEnabled) {
      mongoNodeRepository.nodeChangeCount(nodeInfo.id)
    }
    else {
      changeSetRepository.nodeChangesCount(nodeInfo.id)
    }
    NodeDetailsPage(nodeInfo, buildNodeReferences(nodeInfo), buildNodeIntegrity(nodeInfo), changeCount)
  }

  private def buildNodeMapPage(user: Option[String], nodeInfo: NodeInfo): NodeMapPage = {
    val changeCount = if (mongoEnabled) {
      mongoNodeRepository.nodeChangeCount(nodeInfo.id)
    }
    else {
      changeSetRepository.nodeChangesCount(nodeInfo.id)
    }
    NodeMapPage(
      NodeMapInfo(
        nodeInfo.id,
        nodeInfo.name,
        nodeInfo.names.map(_.networkType),
        nodeInfo.latitude,
        nodeInfo.longitude
      ),
      changeCount
    )
  }

  private def buildNodeChangesPage(user: Option[String], nodeInfo: NodeInfo, parameters: ChangesParameters): NodeChangesPage = {
    if (mongoEnabled) {
      if (user.isDefined) {
        val nodeChanges = mongoNodeRepository.nodeChanges(nodeInfo.id, parameters)
        val changesFilter = mongoNodeRepository.nodeChangesFilter(nodeInfo.id, parameters.year, parameters.month, parameters.day)
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
          nodeInfo,
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
          nodeInfo,
          ChangesFilter.empty,
          Seq.empty,
          incompleteWarning = false,
          0,
          0
        )
      }
    }
    else {
      val changesFilter = changeSetRepository.nodeChangesFilter(nodeInfo.id, parameters.year, parameters.month, parameters.day)
      val totalCount = changesFilter.currentItemCount(parameters.impact)
      val nodeChanges = if (user.isDefined) {
        changeSetRepository.nodeChanges(nodeInfo.id, parameters)
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
      NodeChangesPage(nodeInfo, changesFilter, changes, incompleteWarning, totalCount, changesFilter.totalCount)
    }
  }

  private def buildNodeReferences(nodeInfo: NodeInfo): NodeReferences = {
    val nodeNetworkReferences = nodeRepository.nodeNetworkReferences(nodeInfo.id)
    val nodeOrphanRouteReferences = nodeRepository.nodeOrphanRouteReferences(nodeInfo.id)
    NodeReferences(nodeNetworkReferences, nodeOrphanRouteReferences)
  }

  private def buildNodeIntegrity(nodeInfo: NodeInfo): NodeIntegrity = {
    NodeIntegrity(
      nodeInfo.names.flatMap { nodeName =>
        val tagKey = nodeName.scopedNetworkType.expectedRouteRelationsTag
        nodeInfo.tags(tagKey).map { tagValue =>
          val expectedRouteCount: Int = tagValue.toInt
          val routeRefs = nodeRouteRepository.nodeRouteReferences(nodeName.scopedNetworkType, nodeInfo.id)
          NodeIntegrityDetail(
            nodeName.networkType,
            nodeName.networkScope,
            expectedRouteCount,
            routeRefs
          )
        }
      }
    )
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
