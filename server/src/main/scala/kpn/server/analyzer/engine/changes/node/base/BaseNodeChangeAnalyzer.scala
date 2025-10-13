package kpn.server.analyzer.engine.changes.node.base

import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.ChangeSet
import kpn.api.common.data.raw.RawNode
import kpn.core.analysis.TagInterpreter
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.BlacklistRepository
import org.springframework.stereotype.Component

@Component
class BaseNodeChangeAnalyzer(
  analysisContext: AnalysisContext,
  blacklistRepository: BlacklistRepository
) {

  private val log = Log(classOf[BaseNodeChangeAnalyzer])

  def analyze(changeSet: ChangeSet): ElementChanges = {

    log.debugElapsed {

      val createdNodesById = nodeMap(changeSet, ChangeAction.Create)
      val updatedNodesById = nodeMap(changeSet, ChangeAction.Modify)
      val deletedNodesById = nodeMap(changeSet, ChangeAction.Delete)

      val createdNetworkNodeIds = networkNodeIds(createdNodesById)
      val updatedNetworkNodeIds = networkNodeIds(updatedNodesById)

      val nodeCreateIds1 = withoutKnownNodes(createdNetworkNodeIds)
      val nodeCreateIds2 = withoutKnownNodes(updatedNetworkNodeIds)

      val nodeUpdateIds1 = createdNetworkNodeIds.filter(isKnownNode)
      val nodeUpdateIds2 = updatedNetworkNodeIds.filter(isKnownNode)
      val nodeUpdateIds3 = updatedNodesById.keySet.filter(isKnownNode)

      val nodeDeleteIds1 = networkNodeIds(deletedNodesById)
      val nodeDeleteIds2 = deletedNodesById.keySet.filter(isKnownNode)

      val deletes = nodeDeleteIds1 ++ nodeDeleteIds2
      val updates = nodeUpdateIds1 ++ nodeUpdateIds2 ++ nodeUpdateIds3 -- deletes
      val creates = nodeCreateIds1 ++ nodeCreateIds2 -- updates -- deletes

      val sortedCreates = creates.toList.sorted
      val sortedUpdates = updates.toList.sorted
      val sortedDeletes = deletes.toList.sorted

      (
        s"creates=${creates.size}, updates=${updates.size}, deletes=${deletes.size}",
        ElementChanges(
          sortedCreates,
          sortedUpdates,
          sortedDeletes
        )
      )
    }
  }

  private def nodeMap(changeSet: ChangeSet, action: ChangeAction): Map[Long, RawNode] = {
    changeSet.changes
      .filter(_.action == action)
      .flatMap(_.elements)
      .collect { case e: RawNode => e }
      .filterNot(isBlackListed)
      .map(n => n.id -> n)
      .toMap
  }

  private def networkNodeIds(nodesById: Map[Long, RawNode]): Set[Long] = {
    nodesById.values.
      filter(TagInterpreter.isNetworkNode).
      map(_.id).
      toSet
  }

  private def withoutKnownNodes(nodeIds: Set[Long]): Set[Long] = {
    nodeIds.filterNot(isKnownNode)
  }

  private def isKnownNode(nodeId: Long): Boolean = {
    analysisContext.watched.nodes.contains(nodeId)
  }

  private def isBlackListed(node: RawNode): Boolean = {
    blacklistRepository.get().containsNode(node.id)
  }
}
