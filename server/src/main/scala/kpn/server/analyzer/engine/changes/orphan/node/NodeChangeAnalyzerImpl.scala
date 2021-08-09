package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.changes.ChangeSet
import kpn.api.common.data.raw.RawNode
import kpn.core.analysis.TagInterpreter
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.BlackListRepository
import org.springframework.stereotype.Component

@Component
class NodeChangeAnalyzerImpl(
  analysisContext: AnalysisContext,
  blackListRepository: BlackListRepository
) extends NodeChangeAnalyzer {

  private val log = Log(classOf[NodeChangeAnalyzerImpl])

  def analyze(changeSet: ChangeSet): ElementChanges = {

    log.debugElapsed {

      val createdNodesById = nodeMap(changeSet, Create)
      val updatedNodesById = nodeMap(changeSet, Modify)
      val deletedNodesById = nodeMap(changeSet, Delete)

      val createdNetworkNodeIds = networkNodeIds(createdNodesById)
      val updatedNetworkNodeIds = networkNodeIds(updatedNodesById)

      val nodeCreateIds1 = withoutKnownNodes(createdNetworkNodeIds)
      val nodeCreateIds2 = withoutKnownNodes(updatedNetworkNodeIds)

      val nodeUpdateIds1 = createdNetworkNodeIds.filter(isKnownNode)
      val nodeUpdateIds2 = updatedNetworkNodeIds.filter(isKnownNode)
      val nodeUpdateIds3 = updatedNodesById.keySet.filter(isKnownNode)

      val deletes = deletedNodesById.keySet.filter(isKnownNode)
      val updates = nodeUpdateIds1 ++ nodeUpdateIds2 ++ nodeUpdateIds3 -- deletes
      val creates = nodeCreateIds1 ++ nodeCreateIds2 -- updates -- deletes

      val sortedCreates = creates.toList.sorted
      val sortedUpdates = updates.toList.sorted
      val sortedDeletes = deletes.toList.sorted

      val nodesById = createdNodesById ++ updatedNodesById ++ deletedNodesById

      val nodeCreates = sortedCreates.flatMap(nodesById.get)
      val nodeUpdates = sortedUpdates.flatMap(nodesById.get)
      val nodeDeletes = sortedDeletes.flatMap(nodesById.get)

      val message = s"creates=${creates.size}, updates=${updates.size}, deletes=${deletes.size}"
      val elementChanges = ElementChanges(
        sortedCreates,
        sortedUpdates,
        sortedDeletes
      )

      (message, elementChanges)
    }
  }

  private def nodeMap(changeSet: ChangeSet, action: ChangeAction): Map[Long, RawNode] = {
    changeSet.changes
      .filter(_.action == action)
      .flatMap(_.elements)
      .collect { case e: RawNode => e }
      .map(n => n.id -> n)
      .toMap
  }

  private def networkNodeIds(nodesById: Map[Long, RawNode]): Set[Long] = {
    nodesById.values.
      filter(n => TagInterpreter.isNetworkNode(n.tags)).
      filterNot(isBlackListed).
      map(_.id).
      toSet
  }

  private def withoutKnownNodes(nodeIds: Set[Long]): Set[Long] = {
    nodeIds.filterNot(isKnownNode)
  }

  private def isKnownNode(nodeId: Long): Boolean = {
    analysisContext.data.nodes.watched.contains(nodeId)
  }

  private def isBlackListed(node: RawNode): Boolean = {
    blackListRepository.get.containsRoute(node.id)
  }
}
