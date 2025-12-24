package kpn.server.analyzer.engine.changes.node.base

import kpn.api.custom.Timestamp
import kpn.core.doc.BaseNodeDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.BaseNodeBulkAnalyzer
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class BaseNodeChangeProcessor(
  analysisContext: AnalysisContext,
  baseNodeChangeAnalyzer: BaseNodeChangeAnalyzer,
  baseNodeBulkAnalyzer: BaseNodeBulkAnalyzer,
  nodeRepository: NodeRepository
) extends ChangeProcessor {

  private val log = Log(classOf[BaseNodeChangeProcessor])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {
      val nodeChanges = analyzeChanges(context)
      val (beforeIds, afterIds) = processNodeChanges(nodeChanges, context)
      handleNodeCreationsAndDeletions(nodeChanges, beforeIds, afterIds)
      createResultContext(context, nodeChanges)
    }
  }

  private def analyzeChanges(context: ChangeSetContext): NodeChanges = {
    val nodeElementChanges = baseNodeChangeAnalyzer.analyze(context.changeSet)
    val baseNodeDocsBefore = nodeRepository.baseNodesWithIds(nodeElementChanges.elementIds)
    NodeChanges(nodeElementChanges, baseNodeDocsBefore)
  }

  private def processNodeChanges(nodeChanges: NodeChanges, context: ChangeSetContext): (Set[Long], Set[Long]) = {
    val deletedDocs = processDeletedNodes(nodeChanges)
    val remainingNodeIds = (nodeChanges.nodeIds.toSet -- deletedDocs.map(_._id).toSet).toSeq.sorted
    val baseNodeDocsAfter = processRemainingNodes(context.timestampAfter, remainingNodeIds)

    val beforeNodeIds = nodeChanges.baseNodeDocsBefore.map(_._id).toSet
    val afterNodeIds = baseNodeDocsAfter.filter(_.active).map(_._id).toSet

    (beforeNodeIds, afterNodeIds)
  }

  private def processDeletedNodes(nodeChanges: NodeChanges): Seq[BaseNodeDoc] = {
    val deletedDocs = nodeChanges.elementChanges.deletes.flatMap { nodeId =>
      analysisContext.watched.nodes.remove(nodeId)
      nodeChanges.baseNodeDocsBefore.find(_._id == nodeId).map(deactivateNode)
    }
    nodeRepository.bulkSaveBaseNodes(deletedDocs)
    deletedDocs
  }

  private def deactivateNode(doc: BaseNodeDoc) = {
    doc.copy(
      active = false,
      base = doc.base.copy(
        raw = doc.base.raw.copy(
          tags = Seq.empty,
        ),
        name = None,
        names = Seq.empty
      ),
      facts = Seq.empty,
      tiles = Seq.empty
    )
  }

  private def processRemainingNodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[BaseNodeDoc] = {
    val docs = baseNodeBulkAnalyzer.analyze(timestamp, nodeIds)
    nodeRepository.bulkSaveBaseNodes(docs)
    docs
  }

  private def handleNodeCreationsAndDeletions(
    nodeChanges: NodeChanges,
    beforeNodeIds: Set[Long],
    afterNodeIds: Set[Long]
  ): Unit = {

    val createNodeIds = (afterNodeIds -- beforeNodeIds).toSeq.sorted
    createNodeIds.foreach(analysisContext.watched.nodes.add)

    val deleteNodeIds = (beforeNodeIds -- afterNodeIds).toSeq.distinct.sorted
    deleteNodeIds.foreach(analysisContext.watched.nodes.remove)
  }

  private def createResultContext(context: ChangeSetContext, nodeChanges: NodeChanges): (String, ChangeSetContext) = {
    val updatedContext = context.withImpact(nodeIds = nodeChanges.nodeIds)
    (s"${nodeChanges.nodeIds.size} node changes", updatedContext)
  }

  private case class NodeChanges(
    elementChanges: ElementChanges,
    baseNodeDocsBefore: Seq[BaseNodeDoc]
  ) {
    def nodeIds: Seq[Long] = {
      elementChanges.elementIds
    }
  }
}
