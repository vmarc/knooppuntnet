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
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.builder.NodeChangeInfoBuilder
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.NodeRouteRepository
import org.springframework.stereotype.Component

@Component
class NodePageBuilderImpl(
  nodeRepository: NodeRepository,
  nodeRouteRepository: NodeRouteRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository,
  // old
  mongoEnabled: Boolean
) extends NodePageBuilder {

  def buildDetailsPage(user: Option[String], nodeId: Long): Option[NodeDetailsPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeDetailsPage)
    }
    else {
      if (mongoEnabled) {
        mongoBuildDetailsPage(nodeId)
      }
      else {
        oldBuildDetailsPage(nodeId)
      }
    }
  }

  def buildMapPage(user: Option[String], nodeId: Long): Option[NodeMapPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeMapPage)
    }
    else {
      if (mongoEnabled) {
        mongoBuildMapPage(nodeId)
      }
      else {
        oldBuildMapPage(nodeId)
      }
    }
  }

  def buildChangesPage(user: Option[String], nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage] = {
    if (nodeId == 1) {
      Some(NodePageExample.nodeChangesPage)
    }
    else {
      if (mongoEnabled) {
        mongoBuildChangesPage(user, nodeId, parameters)
      }
      else {
        oldBuildChangesPage(user, nodeId, parameters)
      }
    }
  }

  private def mongoBuildDetailsPage(nodeId: Long): Option[NodeDetailsPage] = {
    nodeRepository.findById(nodeId).map { nodeDoc =>
      val changeCount = changeSetRepository.nodeChangesCount(nodeId)
      val networkReferences = nodeRepository.nodeNetworkReferences(nodeId)
      val mixedNetworkScopes = (
        nodeDoc.names.map(_.networkScope) ++
          nodeDoc.routeReferences.map(_.networkScope) ++
          networkReferences.map(_.networkScope)
        ).distinct.size > 1
      NodeDetailsPage(
        nodeDoc.toInfo,
        mixedNetworkScopes,
        nodeDoc.routeReferences,
        networkReferences,
        nodeDoc.integrity,
        changeCount
      )
    }
  }

  private def oldBuildDetailsPage(nodeId: Long): Option[NodeDetailsPage] = {
    nodeRepository.nodeWithId(nodeId).map { nodeInfo =>
      val filteredFacts = nodeInfo.facts.filter(_ != Fact.IntegrityCheckFailed)
      val filteredNodeInfo = nodeInfo.copy(facts = filteredFacts)
      val changeCount = changeSetRepository.nodeChangesCount(nodeInfo.id)
      val networkReferences = nodeRepository.nodeNetworkReferences(nodeInfo.id)
      val routeReferences = nodeRepository.nodeRouteReferences(nodeInfo.id)
      val mixedNetworkScopes = (
        nodeInfo.names.map(_.networkScope) ++
          routeReferences.map(_.networkScope) ++
          networkReferences.map(_.networkScope)
        ).distinct.size > 1
      NodeDetailsPage(
        filteredNodeInfo,
        mixedNetworkScopes,
        routeReferences,
        networkReferences,
        oldBuildNodeIntegrity(nodeInfo),
        changeCount
      )
    }
  }

  private def mongoBuildMapPage(nodeId: Long): Option[NodeMapPage] = {
    nodeRepository.findById(nodeId).map { nodeDoc =>
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

  private def oldBuildMapPage(nodeId: Long): Option[NodeMapPage] = {
    nodeRepository.nodeWithId(nodeId).map { nodeInfo =>
      val changeCount = changeSetRepository.nodeChangesCount(nodeInfo.id)
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
  }

  private def mongoBuildChangesPage(user: Option[String], nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage] = {
    nodeRepository.findById(nodeId).map { nodeDoc =>
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
          nodeDoc._id,
          nodeDoc.name,
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
          nodeDoc._id,
          nodeDoc.name,
          ChangesFilter.empty,
          Seq.empty,
          incompleteWarning = false,
          0,
          0
        )
      }
    }
  }

  private def oldBuildChangesPage(user: Option[String], nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage] = {
    nodeRepository.nodeWithId(nodeId).map { nodeInfo =>
      val changesFilter = changeSetRepository.nodeChangesFilter(nodeInfo.id, parameters.year, parameters.month, parameters.day)
      val totalCount = changesFilter.currentItemCount(parameters.impact)
      val nodeChanges = if (user.isDefined) {
        changeSetRepository.nodeChanges(nodeInfo.id, parameters)
      }
      else {
        // user is not logged in; we do not show change information
        Seq.empty
      }

      val incompleteWarning = isIncomplete(nodeChanges)
      val changeSetIds = nodeChanges.map(_.key.changeSetId)
      val changeSetInfos = changeSetInfoRepository.all(changeSetIds)
      val changes = nodeChanges.map { nodeChange =>
        new NodeChangeInfoBuilder().build(nodeChange, changeSetInfos)
      }
      NodeChangesPage(
        nodeId,
        nodeInfo.name,
        changesFilter,
        changes,
        incompleteWarning,
        totalCount,
        changesFilter.totalCount
      )
    }
  }

  private def oldBuildNodeIntegrity(nodeInfo: NodeInfo): Option[NodeIntegrity] = {
    val details = nodeInfo.names.flatMap { nodeName =>
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
    if (details.nonEmpty) {
      Some(NodeIntegrity(details))
    }
    else {
      None
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
